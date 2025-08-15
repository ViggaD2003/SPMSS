package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AccountService userService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(new JwtHandshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = null;

                    // 1. Lấy từ header Authorization
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null) {
                        token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
                        log.info("JWT from Authorization header");
                    }

                    // 2. Nếu chưa có thì lấy từ handshake attributes
                    if (token == null) {
                        Object jwtFromQuery = accessor.getSessionAttributes().get("jwt_token");
                        if (jwtFromQuery != null) {
                            token = jwtFromQuery.toString();
                            log.info("JWT from query parameter");
                        }
                    }

                    // 3. Xác thực JWT
                    if (token != null) {
                        try {
                            String username = jwtService.extractUsernameFromJWT(token);
                            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            accessor.setUser(authToken);

                            log.info("User authenticated for WebSocket: {}", username);
                        } catch (Exception e) {
                            log.error("WebSocket authentication failed", e);
                            return null; // reject nếu xác thực thất bại
                        }
                    } else {
                        log.warn("No JWT token found in header or query parameter");
                        return null; // reject nếu không có token
                    }
                }
                return message;
            }
        });
    }

    // Interceptor: Chỉ parse query nếu header không có token
    private class JwtHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response,
                                       WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) {

            try {
                // 1. Kiểm tra Authorization header
                String authHeader = request.getHeaders().getFirst("Authorization");
                if (authHeader != null && !authHeader.isBlank()) {
                    log.info("Authorization header found, skipping query token check");
                    return true; // Cho phép handshake, token sẽ xử lý ở preSend
                }

                // 2. Nếu không có header → lấy token từ query
                URI uri = request.getURI();
                String query = uri.getQuery();

                log.info("WebSocket handshake - URI: {}", uri);
                log.info("WebSocket handshake - Query: {}", query);

                if (query != null) {
                    Map<String, String> params = Arrays.stream(query.split("&"))
                            .map(s -> s.split("=", 2))
                            .filter(arr -> arr.length == 2)
                            .collect(Collectors.toMap(
                                    arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8),
                                    arr -> URLDecoder.decode(arr[1], StandardCharsets.UTF_8)
                            ));

                    String token = params.get("token");
                    if (token != null && !token.isBlank()) {
                        attributes.put("jwt_token", token);
                        log.info("JWT token extracted from query parameter");
                        return true;
                    }
                }

                log.warn("Missing JWT token in both header and query parameters, handshake rejected");
                return false;

            } catch (Exception e) {
                log.error("Error during WebSocket handshake", e);
                return false;
            }
        }

        @Override
        public void afterHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Exception exception) {
            if (exception != null) {
                log.error("WebSocket handshake failed", exception);
            } else {
                log.info("WebSocket handshake completed successfully");
            }
        }
    }
}

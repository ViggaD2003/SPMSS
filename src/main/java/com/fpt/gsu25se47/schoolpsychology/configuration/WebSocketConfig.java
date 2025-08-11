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
import java.util.Map;

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
                .addInterceptors(new JwtHandshakeInterceptor()); // Thêm interceptor để xử lý JWT từ query param
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    log.info("STOMP Command: {}", accessor.getCommand());

                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        String token = null;

                        // 1. Thử lấy từ header Authorization trước (ưu tiên)
                        String authHeader = accessor.getFirstNativeHeader("Authorization");
                        if (authHeader != null) {
                            token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
                            log.info("JWT from Authorization header");
                        }

                        // 2. Nếu không có trong header, lấy từ session attributes (được set từ HandshakeInterceptor)
                        if (token == null) {
                            Object jwtFromQuery = accessor.getSessionAttributes().get("jwt_token");
                            if (jwtFromQuery != null) {
                                token = jwtFromQuery.toString();
                                log.info("JWT from query parameter");
                            }
                        }

                        // 3. Xác thực JWT token
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
                                return null; // Reject connection if authentication fails
                            }
                        } else {
                            log.warn("No JWT token found in header or query parameter");
                            return null; // Reject connection if no token
                        }
                    }
                }
                return message;
            }
        });
    }

    // Inner class để xử lý JWT từ query parameter trong handshake
    private class JwtHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response,
                                       WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) throws Exception {

            try {
                URI uri = request.getURI();
                String query = uri.getQuery();

                log.info("WebSocket handshake - URI: {}", uri.toString());
                log.info("WebSocket handshake - Query: {}", query);

                // Parse query parameters để lấy JWT token
                if (query != null && query.contains("token=")) {
                    String[] params = query.split("&");
                    for (String param : params) {
                        if (param.startsWith("token=")) {
                            String token = URLDecoder.decode(param.substring(6), StandardCharsets.UTF_8);
                            // Lưu token vào session attributes để dùng trong ChannelInterceptor
                            attributes.put("jwt_token", token);
                            log.info("JWT token extracted from query parameter");
                            break;
                        }
                    }
                }

                // Cho phép handshake tiếp tục (authentication sẽ được xử lý trong ChannelInterceptor)
                return true;

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
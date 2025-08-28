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
                log.info("📩 Incoming frame: {}", accessor != null ? accessor.getCommand() : "null");

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = (String) accessor.getSessionAttributes().get("jwt_token");
                    System.out.printf(token );

                    log.info("🔍 Token from CONNECT: {}", token);

                    if (token != null) {
                        try {
                            String username = jwtService.extractUsernameFromJWT(token);
                            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            accessor.setUser(authToken);

                            log.info("✅ WebSocket user authenticated: {}", username);
                        } catch (Exception e) {
                            log.error("❌ WebSocket authentication failed: {}", e.getMessage());
                            return null; // reject CONNECT
                        }
                    } else {
                        log.warn("❌ No JWT token found in handshake attributes");
                        return null;
                    }
                }
                return message;
            }
        });
    }

    /**
     * Interceptor lấy JWT từ query param ?token=...
     */
    private class JwtHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response,
                                       WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) {
            try {
                URI uri = request.getURI();
                String query = uri.getQuery();
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
                        log.info("✅ JWT token extracted from query parameter");
                        return true;
                    }
                }

                log.warn("❌ Missing JWT token in query parameter, handshake rejected");
                return false;

            } catch (Exception e) {
                log.error("❌ Error during WebSocket handshake", e);
                return false;
            }
        }

        @Override
        public void afterHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Exception exception) {
            if (exception != null) {
                log.error("❌ WebSocket handshake failed", exception);
            } else {
                log.info("✅ WebSocket handshake completed successfully");
            }
        }
    }
}

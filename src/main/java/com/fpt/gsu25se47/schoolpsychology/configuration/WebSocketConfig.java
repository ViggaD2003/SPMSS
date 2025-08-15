package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.socket.config.annotation.*;

import java.net.URLDecoder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwtService; // Service xác thực JWT
    @Autowired
    private UserService userService; // Service load UserDetails

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(new JwtHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Interceptor để lấy JWT token từ query parameter
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
                        log.info("✅ JWT token received from query param");
                        return true;
                    }
                }
                log.warn("❌ Missing JWT token in query parameter");
                return false; // Từ chối nếu không có token
            } catch (Exception e) {
                log.error("❌ Error parsing JWT token from query", e);
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

    /**
     * Xác thực user khi nhận CONNECT frame từ STOMP
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = (String) accessor.getSessionAttributes().get("jwt_token");
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
                            return null; // Reject CONNECT
                        }
                    } else {
                        log.warn("❌ No JWT token found in session attributes");
                        return null;
                    }
                }
                return message;
            }
        });
    }
}

package com.fpt.gsu25se47.schoolpsychology.service.impl;


import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration.access-token}")
    private long accessExpiration;

    @Value("${jwt.expiration.refresh-token}")
    private long refreshExpiration;

    private final AccountRepository accountRepo;

    @Override
    public String extractUsernameFromJWT(String jwt) {
        return getClaim(jwt, Claims::getSubject);
    }

    @Override
    public String extractRoleFromJWT(String jwt) {
        Claims claims = extractAllClaimsFromToken(jwt);
        return claims.get("role", String.class);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaimsFromToken(token));
    }

    private Claims extractAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return e.getClaims(); // vẫn lấy được phần payload
        }
    }

    private Key getSigninKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    public String generateAccessToken(UserDetails user) {
        return generateToken(new HashMap<>(), user, accessExpiration);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateToken(new HashMap<>(), user, refreshExpiration);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails user, long expiredTime) {
        Account account = accountRepo.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        extraClaims.put("role", populateAuthorities(user.getAuthorities()));
        extraClaims.put("fullname", account.getFullName());
        extraClaims.put("user-id", account.getId());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean checkIfExpired(String jwt) {
        try {
            Date expiration = getClaim(jwt, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Nếu token đã hết hạn, thì return true
            return true;
        } catch (Exception e) {
            // Nếu lỗi khác (ví dụ sai định dạng), thì log hoặc throw tiếp
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

}
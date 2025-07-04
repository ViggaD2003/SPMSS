package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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

    private final StudentRepository studentRepo;

    private final RelationshipRepository relationshipRepo;

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
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
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

        Student student = null;

        if(account.getRole().name().equals("PARENTS")){
            List<Student> students = relationshipRepo.findChildrenByParentAccountId(account.getId());

            List<Map<String, Object>> childrenClaims = students.stream().map(child -> {
                Map<String, Object> map = new HashMap<>();
                map.put("userId", child.getId());
                map.put("teacherId", child.getClasses().getTeacher().getId());
                map.put("fullName", child.getAccount().getFullName());
                map.put("isEnable", child.getIsEnableSurvey());
                return map;
            }).toList();

            extraClaims.put("relationship_type", "PARENT");
            extraClaims.put("children", childrenClaims);
        } else if(account.getRole().name().equals("STUDENT")){
             student = studentRepo.findById(account.getId()).orElseThrow(() -> new RuntimeException("Unauthorized"));
        }

        JwtBuilder builder = Jwts.builder()
                .setClaims(extraClaims)
                .claim("role", populateAuthorities(user.getAuthorities()))
                .claim("fullname", account.getFullName())
                .claim("user-id", account.getId())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime));

        if (student != null) {
            builder.claim("isEnableSurvey", student.getIsEnableSurvey());
        }

        return builder
                .signWith(getSigninKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean checkIfNotExpired(String jwt) {
        return !getClaim(jwt, Claims::getExpiration).before(new Date());
    }
}
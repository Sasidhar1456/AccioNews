package com.example.AccioNews.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final long ACCESS_EXPIRATION = 1000 * 60 * 15; // 15 mins
    private final long REFRESH_EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    public String generateAccessToken(Long userId, String userName) {
        String token = Jwts.builder()
    .claim("userId", userId) 
    .claim("userName", userName)
    .setIssuedAt(new Date(System.currentTimeMillis()))
    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
    .compact();

        return token;

    }

    public String generateRefreshToken(Long userId, String userName) {
        return Jwts.builder()
            .claim("userId", userId)
            .claim("userName", userName)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).get("userName", String.class);
    }

    public boolean validateToken(String token) {
        return !extractExpiration(token).toInstant().isBefore(Instant.now());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

}

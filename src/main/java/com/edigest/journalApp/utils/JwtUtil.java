package com.edigest.journalApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    // 🔐 Strong secret key (at least 32 chars for HS256)
    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey1234567890!@#$%^&*()}{:";

    // 🔑 Signing key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 🛠️ Generate token (public method)
    public String generateToken(String username) {
        return createToken(Map.of(), username);
    }

    // 🛠️ Create token (internal use)
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 min
                .signWith(getSigningKey())
                .compact();
    }

    // 📥 Extract username
    public String extractUsername(String token) {
        Claims claims =  extractAllClaims(token);
        return claims.getSubject();
    }

    // 📥 Extract expiration
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // ⛔ Check if token expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 🔍 Extract all claims (FIXED version)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

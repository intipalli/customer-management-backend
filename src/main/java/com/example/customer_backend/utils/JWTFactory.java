package com.example.customer_backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Date;

public class JWTFactory {
    private static final String SECRET_KEY = "secret";

    // Create a JWT token without scopes
    public static String createToken(String username) {
        long expirationTime = 1000 * 60 * 60 * 5; // 5 hours
        return Jwts.builder()
            .setSubject(username)
            .setIssuer("me@me.com")
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    // Verify a JWT token
    public static boolean verifyToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

    // Get all claims from a JWT token
    public static Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
    }
}

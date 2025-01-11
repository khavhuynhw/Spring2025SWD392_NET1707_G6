package com.net1707.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JWTService {

    private static final Logger log = LoggerFactory.getLogger(JWTService.class);

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(
                StandardCharsets.UTF_8
        );
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        String name = authentication.getName();
        String scope = String.valueOf(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return Jwts.builder()
                .setSubject(name)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Token valid for 30 minutes
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .claim("scope",scope)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch(JwtException e){
            log.info("Invalid token" + e.getMessage());
        }
        catch(Exception e){
            log.info("Unexpected error while parsing token: {}" + e.getMessage());
        }
        return null;
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

            // Get 'iat' (Issued At) from the token
            Date issuedAt = claims.getIssuedAt();
            Instant issuedAtInstant = issuedAt.toInstant();

            // Compare 'iat' with the current time
            Instant now = Instant.now();
            return issuedAtInstant.isBefore(now);
        } catch (Exception e) {
            // Token is invalid
            return false;
        }
    }
}

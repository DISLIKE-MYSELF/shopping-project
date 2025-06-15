package com.example.backend.utils;

import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private final long EXPIRATION_MS = 86400000; // 24小时

  private final Key SIGNING_KEY;

  public JwtUtils(@Value("${jwt.secret.key}") String secretKey) {
    this.SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public static String sanitize(String token) {
    return token.trim().replaceAll("\\s", "");
  }

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)).signWith(SIGNING_KEY)
        .compact();
  }

  public String generateToken(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)).signWith(SIGNING_KEY)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(sanitize(token))
        .getBody().getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(sanitize(token));
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(sanitize(token)));
  }

  private boolean isTokenExpired(String token) {
    return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(sanitize(token))
        .getBody().getExpiration().before(new Date());
  }
}

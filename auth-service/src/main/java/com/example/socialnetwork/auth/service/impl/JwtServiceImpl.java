package com.example.socialnetwork.auth.service.impl;

import com.example.socialnetwork.auth.security.JwtPrincipal;
import com.example.socialnetwork.auth.security.JwtProperties;
import com.example.socialnetwork.auth.security.JwtTokenType;
import com.example.socialnetwork.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
  private final JwtProperties properties;

  @Override
  public String generateAccessToken(JwtPrincipal principal) {
    return generateToken(
      principal,
      JwtTokenType.ACCESS,
      properties.accessTokenExpiration()
    );
  }

  @Override
  public String generateRefreshToken(JwtPrincipal principal) {
    return generateToken(
      principal,
      JwtTokenType.REFRESH,
      properties.refreshTokenExpiration()
    );
  }

  @Override
  public JwtPrincipal extractPrincipal(String token) {
    Claims claims = parseClaims(token);

    return new JwtPrincipal(
      UUID.fromString(claims.getSubject())
    );
  }

  @Override
  public JwtTokenType extractTokenType(String token) {
    Claims claims = parseClaims(token);

    return JwtTokenType.valueOf(
      claims.get("type", String.class)
    );
  }

  @Override
  public boolean isValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private String generateToken(
    JwtPrincipal principal,
    JwtTokenType type,
    long expiration
  ) {
    Instant now = Instant.now();

    return Jwts.builder()
      .subject(principal.publicId().toString())
      .id(UUID.randomUUID().toString())
      .claim("type", type.name())
      .issuedAt(Date.from(now))
      .expiration(
        Date.from(now.plusSeconds(expiration))
      )
      .signWith(signingKey())
      .compact();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
      .verifyWith(signingKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  private SecretKey signingKey() {
    return Keys.hmacShaKeyFor(
      properties.secret()
        .getBytes(StandardCharsets.UTF_8)
    );
  }
}
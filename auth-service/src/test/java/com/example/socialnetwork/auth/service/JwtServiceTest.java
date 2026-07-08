package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.jwt.JwtPrincipal;
import com.example.socialnetwork.auth.jwt.JwtProperties;
import com.example.socialnetwork.auth.jwt.JwtTokenType;
import com.example.socialnetwork.auth.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
  private final JwtService jwtService =
    new JwtServiceImpl(
      new JwtProperties(
        "ReplaceWithLongRandomSecretAtLeast32Characters",
        900,
        2592000
      )
    );

  @Test
  void shouldGenerateAndExtractAccessToken() {
    UUID userId = UUID.randomUUID();

    String token = jwtService.generateAccessToken(
      new JwtPrincipal(userId)
    );

    assertNotNull(token);
    assertTrue(jwtService.isValid(token));

    JwtPrincipal principal = jwtService.extractPrincipal(token);

    assertEquals(
      userId,
      principal.publicId()
    );

    assertEquals(JwtTokenType.ACCESS, jwtService.extractTokenType(token));
  }

  @Test
  void shouldGenerateRefreshToken() {
    String token = jwtService.generateRefreshToken(
      new JwtPrincipal(UUID.randomUUID())
    );

    assertTrue(jwtService.isValid(token));
    assertEquals(JwtTokenType.REFRESH, jwtService.extractTokenType(token));
  }
}
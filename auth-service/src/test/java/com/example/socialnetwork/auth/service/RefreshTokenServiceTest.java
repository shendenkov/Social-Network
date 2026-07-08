package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.RefreshToken;
import com.example.socialnetwork.auth.jwt.JwtProperties;
import com.example.socialnetwork.auth.repository.RefreshTokenRepository;
import com.example.socialnetwork.auth.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
  @Mock
  private RefreshTokenRepository repository;
  private RefreshTokenService service;

  @BeforeEach
  void setUp() {
    service = new RefreshTokenServiceImpl(
      new JwtProperties(
        "ReplaceWithLongRandomSecretAtLeast32Characters",
        900,
        2592000
      ),
      repository
    );
  }

  @Test
  void shouldSaveHashedRefreshToken() {
    Credential credential = Credential.builder()
      .id(1L)
      .build();

    when(repository.save(any()))
      .thenAnswer(invocation -> invocation.getArgument(0));

    RefreshToken token =
      service.create(
        credential,
        "refresh-token"
      );

    assertEquals(credential, token.getCredential());
    assertFalse(token.isRevoked());
    assertNotEquals("refresh-token", token.getTokenHash());
    assertEquals(64, token.getTokenHash().length());

    verify(repository).save(any());
  }

  @Test
  void shouldSearchByHashedToken() {
    service.findByToken("refresh-token");

    verify(repository).findByTokenHash(any());
  }

  @Test
  void shouldRevokeToken() {
    RefreshToken token = RefreshToken.builder()
      .revoked(false)
      .build();

    service.revoke(token);

    assertTrue(token.isRevoked());
  }
}
package com.example.socialnetwork.auth.service.impl;

import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.RefreshToken;
import com.example.socialnetwork.auth.security.jwt.JwtProperties;
import com.example.socialnetwork.auth.repository.RefreshTokenRepository;
import com.example.socialnetwork.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private final JwtProperties jwtProperties;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public RefreshToken create(
    Credential credential,
    String refreshToken
  ) {
    RefreshToken entity = RefreshToken.builder()
      .credential(credential)
      .tokenHash(
        hashToken(refreshToken)
      )
      .expiresAt(
        Instant.now()
          .plusSeconds(
            jwtProperties.refreshTokenExpiration()
          )
      )
      .revoked(false)
      .build();

    return refreshTokenRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RefreshToken> findByToken(String refreshToken) {
    return refreshTokenRepository.findByTokenHash(
      hashToken(refreshToken)
    );
  }

  @Override
  public void revoke(RefreshToken refreshToken) {
    refreshToken.setRevoked(true);
  }

  private String hashToken(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 algorithm is not available", e);
    }
  }
}
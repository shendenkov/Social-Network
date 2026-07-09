package com.example.socialnetwork.auth.service.impl;

import com.example.socialnetwork.auth.dto.request.LoginRequest;
import com.example.socialnetwork.auth.dto.request.LogoutRequest;
import com.example.socialnetwork.auth.dto.request.RefreshRequest;
import com.example.socialnetwork.auth.dto.request.RegisterRequest;
import com.example.socialnetwork.auth.dto.response.LoginResponse;
import com.example.socialnetwork.auth.dto.response.RefreshResponse;
import com.example.socialnetwork.auth.dto.response.RegisterResponse;
import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.RefreshToken;
import com.example.socialnetwork.auth.entity.enums.AccountStatus;
import com.example.socialnetwork.auth.event.UserRegisteredEvent;
import com.example.socialnetwork.auth.exception.EmailAlreadyExistsException;
import com.example.socialnetwork.auth.exception.InvalidCredentialsException;
import com.example.socialnetwork.auth.exception.InvalidRefreshTokenException;
import com.example.socialnetwork.auth.security.JwtPrincipal;
import com.example.socialnetwork.auth.security.JwtProperties;
import com.example.socialnetwork.auth.security.JwtTokenType;
import com.example.socialnetwork.auth.mapper.CredentialMapper;
import com.example.socialnetwork.auth.repository.CredentialRepository;
import com.example.socialnetwork.auth.service.AuthService;
import com.example.socialnetwork.auth.service.JwtService;
import com.example.socialnetwork.auth.service.OutboxService;
import com.example.socialnetwork.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
  private final JwtProperties jwtProperties;
  private final CredentialRepository credentialRepository;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final OutboxService outboxService;
  private final CredentialMapper credentialMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public RegisterResponse register(RegisterRequest request) {
    Credential credential = credentialMapper.toCredential(request);
    credential.setPasswordHash(passwordEncoder.encode(request.password()));
    credential.setStatus(AccountStatus.ACTIVE);

    try {
      credential = credentialRepository.save(credential);
    } catch (DataIntegrityViolationException ex) {
      throw new EmailAlreadyExistsException(request.email());
    }

    outboxService.save(
      "Credential",
      credential.getPublicId().toString(),
      new UserRegisteredEvent(
        UUID.randomUUID(),
        credential.getPublicId()
      )
    );

    return credentialMapper.toRegisterResponse(credential);
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    Credential credential =
      credentialRepository.findByEmail(request.email())
        .orElseThrow(InvalidCredentialsException::new);

    if (!passwordEncoder.matches(
      request.password(),
      credential.getPasswordHash()
    )) {
      throw new InvalidCredentialsException();
    }

    JwtPrincipal principal = new JwtPrincipal(credential.getPublicId());
    String accessToken = jwtService.generateAccessToken(principal);
    String refreshToken = jwtService.generateRefreshToken(principal);

    refreshTokenService.create(
      credential,
      refreshToken
    );

    return new LoginResponse(
      accessToken,
      refreshToken,
      jwtProperties.accessTokenExpiration()
    );
  }

  @Override
  public RefreshResponse refresh(RefreshRequest request) {
    String refreshTokenValue = request.refreshToken();

    if (jwtService.extractTokenType(refreshTokenValue) != JwtTokenType.REFRESH) {
      throw new InvalidRefreshTokenException();
    }

    RefreshToken refreshToken =
      refreshTokenService.findByToken(refreshTokenValue)
        .orElseThrow(InvalidRefreshTokenException::new);

    if (refreshToken.isRevoked()) {
      throw new InvalidRefreshTokenException();
    }
    if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
      throw new InvalidRefreshTokenException();
    }

    JwtPrincipal principal = jwtService.extractPrincipal(refreshTokenValue);
    Credential credential =
      credentialRepository.findByPublicId(principal.publicId())
        .orElseThrow(InvalidRefreshTokenException::new);

    String accessToken = jwtService.generateAccessToken(principal);
    String newRefreshToken = jwtService.generateRefreshToken(principal);

    refreshTokenService.revoke(refreshToken);
    refreshTokenService.create(
      credential,
      newRefreshToken
    );

    return new RefreshResponse(
      accessToken,
      newRefreshToken,
      jwtProperties.accessTokenExpiration()
    );
  }

  @Override
  public void logout(LogoutRequest request) {
    RefreshToken refreshToken =
      refreshTokenService.findByToken(request.refreshToken())
        .orElseThrow(InvalidRefreshTokenException::new);

    refreshTokenService.revoke(refreshToken);
  }
}
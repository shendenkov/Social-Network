package com.example.socialnetwork.auth.service.impl;

import com.example.socialnetwork.auth.dto.request.LoginRequest;
import com.example.socialnetwork.auth.dto.request.RegisterRequest;
import com.example.socialnetwork.auth.dto.response.LoginResponse;
import com.example.socialnetwork.auth.dto.response.RegisterResponse;
import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.enums.AccountStatus;
import com.example.socialnetwork.auth.exception.EmailAlreadyExistsException;
import com.example.socialnetwork.auth.exception.InvalidCredentialsException;
import com.example.socialnetwork.auth.jwt.JwtPrincipal;
import com.example.socialnetwork.auth.jwt.JwtProperties;
import com.example.socialnetwork.auth.mapper.CredentialMapper;
import com.example.socialnetwork.auth.repository.CredentialRepository;
import com.example.socialnetwork.auth.service.AuthService;
import com.example.socialnetwork.auth.service.JwtService;
import com.example.socialnetwork.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
  private final JwtProperties jwtProperties;
  private final CredentialRepository credentialRepository;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
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
}
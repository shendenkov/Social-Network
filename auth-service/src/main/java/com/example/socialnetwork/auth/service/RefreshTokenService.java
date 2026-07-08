package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
  RefreshToken create(
    Credential credential,
    String refreshToken
  );
  Optional<RefreshToken> findByToken(String refreshToken);
  void revoke(RefreshToken token);
}
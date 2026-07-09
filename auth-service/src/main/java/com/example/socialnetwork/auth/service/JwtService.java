package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.security.jwt.JwtPrincipal;
import com.example.socialnetwork.auth.security.jwt.JwtTokenType;

public interface JwtService {
  String generateAccessToken(JwtPrincipal principal);
  String generateRefreshToken(JwtPrincipal principal);
  boolean isValid(String token);
  JwtTokenType extractTokenType(String token);
  JwtPrincipal extractPrincipal(String token);
}
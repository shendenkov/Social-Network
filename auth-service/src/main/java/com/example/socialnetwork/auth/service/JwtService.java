package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.security.JwtPrincipal;
import com.example.socialnetwork.auth.security.JwtTokenType;

public interface JwtService {
  String generateAccessToken(JwtPrincipal principal);
  String generateRefreshToken(JwtPrincipal principal);
  boolean isValid(String token);
  JwtTokenType extractTokenType(String token);
  JwtPrincipal extractPrincipal(String token);
}
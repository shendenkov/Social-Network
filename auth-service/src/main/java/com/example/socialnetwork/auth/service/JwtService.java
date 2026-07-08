package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.jwt.JwtPrincipal;
import com.example.socialnetwork.auth.jwt.JwtTokenType;

public interface JwtService {
  String generateAccessToken(JwtPrincipal principal);
  String generateRefreshToken(JwtPrincipal principal);
  boolean isValid(String token);
  JwtTokenType extractTokenType(String token);
  JwtPrincipal extractPrincipal(String token);
}
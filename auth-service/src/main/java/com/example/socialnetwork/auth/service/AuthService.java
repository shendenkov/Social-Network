package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.dto.request.LoginRequest;
import com.example.socialnetwork.auth.dto.request.RegisterRequest;
import com.example.socialnetwork.auth.dto.response.LoginResponse;
import com.example.socialnetwork.auth.dto.response.RegisterResponse;

public interface AuthService {
  RegisterResponse register(RegisterRequest request);
  LoginResponse login(LoginRequest request);
}
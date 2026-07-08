package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.dto.request.RegisterRequest;
import com.example.socialnetwork.auth.dto.response.RegisterResponse;

public interface AuthService {
  RegisterResponse register(RegisterRequest request);
}
package com.example.socialnetwork.auth.controller;

import com.example.socialnetwork.auth.dto.request.LoginRequest;
import com.example.socialnetwork.auth.dto.request.LogoutRequest;
import com.example.socialnetwork.auth.dto.request.RefreshRequest;
import com.example.socialnetwork.auth.dto.request.RegisterRequest;
import com.example.socialnetwork.auth.dto.response.LoginResponse;
import com.example.socialnetwork.auth.dto.response.RefreshResponse;
import com.example.socialnetwork.auth.dto.response.RegisterResponse;
import com.example.socialnetwork.auth.security.jwt.JwtPrincipal;
import com.example.socialnetwork.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }

  @PostMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  public RefreshResponse refresh(@Valid @RequestBody RefreshRequest request) {
    return authService.refresh(request);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(@Valid @RequestBody LogoutRequest request) {
    authService.logout(request);
  }

  @GetMapping("/me")
  @ResponseStatus(HttpStatus.OK)
  public JwtPrincipal me(@AuthenticationPrincipal JwtPrincipal principal) {
    return principal;
  }
}
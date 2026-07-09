package com.example.socialnetwork.auth.security;

import com.example.socialnetwork.auth.security.jwt.JwtPrincipal;
import com.example.socialnetwork.auth.security.jwt.JwtTokenType;
import com.example.socialnetwork.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    try {
      if (jwtService.extractTokenType(token) != JwtTokenType.ACCESS) {
        filterChain.doFilter(request, response);
        return;
      }

      JwtPrincipal principal = jwtService.extractPrincipal(token);

      UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
          principal,
          null,
          null
        );

      SecurityContextHolder
        .getContext()
        .setAuthentication(authentication);
    } catch (RuntimeException ignored) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}
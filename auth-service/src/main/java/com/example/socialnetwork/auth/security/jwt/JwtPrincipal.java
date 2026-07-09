package com.example.socialnetwork.auth.security.jwt;

import java.util.UUID;

public record JwtPrincipal(
  UUID publicId
) {
}
package com.example.socialnetwork.auth.jwt;

import java.util.UUID;

public record JwtPrincipal(
  UUID publicId
) {
}
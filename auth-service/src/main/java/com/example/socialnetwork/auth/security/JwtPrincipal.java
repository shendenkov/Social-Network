package com.example.socialnetwork.auth.security;

import java.util.UUID;

public record JwtPrincipal(
  UUID publicId
) {
}
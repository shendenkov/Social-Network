package com.example.socialnetwork.auth.dto.response;

import java.util.UUID;

public record RegisterResponse(
  UUID id,
  String email
) {
}
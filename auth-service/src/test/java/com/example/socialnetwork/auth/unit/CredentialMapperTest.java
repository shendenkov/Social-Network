package com.example.socialnetwork.auth.unit;

import com.example.socialnetwork.auth.dto.response.RegisterResponse;
import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.enums.AccountStatus;
import com.example.socialnetwork.auth.mapper.CredentialMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CredentialMapperTest {
  private final CredentialMapper mapper = Mappers.getMapper(CredentialMapper.class);

  @Test
  void shouldMapCredentialToRegisterResponse() {
    Credential credential = Credential.builder()
      .publicId(UUID.randomUUID())
      .email("test@example.com")
      .status(AccountStatus.ACTIVE)
      .build();

    RegisterResponse response = mapper.toRegisterResponse(credential);

    assertEquals(credential.getPublicId(), response.id());
    assertEquals(credential.getEmail(), response.email());
  }
}
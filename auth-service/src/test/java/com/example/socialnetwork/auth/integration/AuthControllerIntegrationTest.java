package com.example.socialnetwork.auth.integration;

import com.example.socialnetwork.auth.dto.request.LoginRequest;
import com.example.socialnetwork.auth.dto.response.LoginResponse;
import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.RefreshToken;
import com.example.socialnetwork.auth.entity.enums.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIntegrationTest extends BaseIntegrationTest {
  //@Test
  void shouldLoginSuccessfully() throws Exception {
    Credential credential = Credential.builder()
      .publicId(UUID.randomUUID())
      .email("test@example.com")
      .passwordHash(passwordEncoder.encode("password"))
      .status(AccountStatus.ACTIVE)
      .build();

    credential = credentialRepository.save(credential);

    LoginRequest request = new LoginRequest(
      "test@example.com",
      "password"
    );

    MvcResult mvcResult = mockMvc.perform(
        post("/api/v1/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk())
      .andReturn();

    LoginResponse response = objectMapper.readValue(
      mvcResult.getResponse().getContentAsString(),
      LoginResponse.class
    );

    assertNotNull(response.accessToken());
    assertNotNull(response.refreshToken());
    assertFalse(response.accessToken().isBlank());
    assertFalse(response.refreshToken().isBlank());
    assertEquals(jwtProperties.accessTokenExpiration(), response.expiresIn());

    List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByCredential(credential);

    assertEquals(1, refreshTokens.size());

    RefreshToken refreshToken = refreshTokens.getFirst();

    assertFalse(refreshToken.isRevoked());
    assertEquals(credential.getId(), refreshToken.getCredential().getId());
    assertNotEquals(response.refreshToken(), refreshToken.getTokenHash());
    assertTrue(refreshToken.getExpiresAt().isAfter(Instant.now()));
  }
}
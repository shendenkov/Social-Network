package com.example.socialnetwork.auth.integration;

import com.example.socialnetwork.auth.security.jwt.JwtProperties;
import com.example.socialnetwork.auth.repository.CredentialRepository;
import com.example.socialnetwork.auth.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
  @Autowired
  protected JwtProperties jwtProperties;

  @Autowired
  protected CredentialRepository credentialRepository;

  @Autowired
  protected RefreshTokenRepository refreshTokenRepository;

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MockMvc mockMvc;

  @Container
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

  @DynamicPropertySource
  static void configure(DynamicPropertyRegistry registry) {
    registry.add(
      "spring.datasource.url",
      postgres::getJdbcUrl
    );
    registry.add(
      "spring.datasource.username",
      postgres::getUsername
    );
    registry.add(
      "spring.datasource.password",
      postgres::getPassword
    );
  }

  @BeforeEach
  void cleanDatabase() {
    refreshTokenRepository.deleteAll();
    credentialRepository.deleteAll();
  }
}
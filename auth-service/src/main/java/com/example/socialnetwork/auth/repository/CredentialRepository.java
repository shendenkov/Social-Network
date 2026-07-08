package com.example.socialnetwork.auth.repository;

import com.example.socialnetwork.auth.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
  boolean existsByEmail(String email);
  Optional<Credential> findByEmail(String email);
  Optional<Credential> findByPublicId(UUID publicId);
}
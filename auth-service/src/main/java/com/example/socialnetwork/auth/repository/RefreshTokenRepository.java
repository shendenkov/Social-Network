package com.example.socialnetwork.auth.repository;

import com.example.socialnetwork.auth.entity.Credential;
import com.example.socialnetwork.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  List<RefreshToken> findAllByCredential(Credential credential);
  void deleteAllByCredential(Credential credential);
}
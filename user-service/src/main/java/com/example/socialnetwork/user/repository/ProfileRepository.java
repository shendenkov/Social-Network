package com.example.socialnetwork.user.repository;

import com.example.socialnetwork.user.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
  boolean existsByUsername(String username);
  Optional<Profile> findByUserId(UUID userId);
  Optional<Profile> findByUsername(String username);
}
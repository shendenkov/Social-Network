package com.example.socialnetwork.auth.entity;

import com.example.socialnetwork.auth.entity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credential extends BaseAuditableEntity {
  @Column(name = "public_id", nullable = false, unique = true)
  private UUID publicId;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccountStatus status;
}
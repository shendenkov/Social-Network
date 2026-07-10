package com.example.socialnetwork.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProcessedEvent {
  @Id
  private UUID eventId;

  @Column(nullable = false)
  private Instant processedAt;
}
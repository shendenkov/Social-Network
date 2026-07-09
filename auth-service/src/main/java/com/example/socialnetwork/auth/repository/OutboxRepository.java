package com.example.socialnetwork.auth.repository;

import com.example.socialnetwork.auth.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
  List<OutboxEvent> findTop100ByPublishedAtIsNullOrderByCreatedAt();
}
package com.example.socialnetwork.auth.service.impl;

import com.example.socialnetwork.auth.entity.OutboxEvent;
import com.example.socialnetwork.auth.event.DomainEvent;
import com.example.socialnetwork.auth.repository.OutboxRepository;
import com.example.socialnetwork.auth.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class OutboxServiceImpl implements OutboxService {
  private final ObjectMapper objectMapper;
  private final OutboxRepository outboxRepository;

  @Override
  public void save(
    String aggregateType,
    String aggregateId,
    DomainEvent event
  ) {
    try {
      OutboxEvent outboxEvent = OutboxEvent.builder()
        .eventId(event.eventId())
        .aggregateType(aggregateType)
        .aggregateId(aggregateId)
        .eventType(event.getClass().getSimpleName())
        .payload(
          objectMapper.writeValueAsString(event)
        )
        .createdAt(Instant.now())
        .retryCount(0)
        .build();

      outboxRepository.save(outboxEvent);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to serialize outbox event", e);
    }
  }
}
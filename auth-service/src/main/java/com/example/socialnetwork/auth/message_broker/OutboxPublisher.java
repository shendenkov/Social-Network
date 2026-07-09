package com.example.socialnetwork.auth.message_broker;

import com.example.socialnetwork.auth.entity.OutboxEvent;
import com.example.socialnetwork.auth.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {
  private final OutboxRepository outboxRepository;
  private final EventPublisher eventPublisher;

  @Transactional
  @Scheduled(fixedDelayString = "${outbox.publish-delay:1000}")
  public void publish() {
    List<OutboxEvent> events = outboxRepository.findTop100ByPublishedAtIsNullOrderByCreatedAt();

    for (OutboxEvent event : events) {
      try {
        eventPublisher.publish(event);
        event.setPublishedAt(Instant.now());
      } catch (Exception ex) {
        event.setRetryCount(
          event.getRetryCount() + 1
        );

        log.error("Failed to publish outbox event {}", event.getEventId(), ex);
      }
    }
  }
}
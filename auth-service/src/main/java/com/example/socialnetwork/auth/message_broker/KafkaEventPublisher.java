package com.example.socialnetwork.auth.message_broker;

import com.example.socialnetwork.auth.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void publish(OutboxEvent event) {
    try {
      kafkaTemplate.send(
        event.getEventType(),
        event.getEventId().toString(),
        event.getPayload()
      ).get();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to publish Kafka event", e);
    }
  }
}
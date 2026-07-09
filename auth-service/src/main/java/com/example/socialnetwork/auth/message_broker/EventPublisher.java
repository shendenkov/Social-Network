package com.example.socialnetwork.auth.message_broker;

import com.example.socialnetwork.auth.entity.OutboxEvent;

public interface EventPublisher {
  void publish(OutboxEvent event);
}
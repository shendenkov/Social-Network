package com.example.socialnetwork.auth.service;

import com.example.socialnetwork.auth.event.DomainEvent;

public interface OutboxService {
  void save(
    String aggregateType,
    String aggregateId,
    DomainEvent event
  );
}
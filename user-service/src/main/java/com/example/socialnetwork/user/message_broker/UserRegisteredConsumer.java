package com.example.socialnetwork.user.message_broker;

import com.example.socialnetwork.user.event.UserRegisteredEvent;
import com.example.socialnetwork.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredConsumer {
  private final ProfileService profileService;

  @KafkaListener(topics = "UserRegisteredEvent")
  public void consume(UserRegisteredEvent event) {
    profileService.createProfile(event);
  }
}
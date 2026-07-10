package com.example.socialnetwork.user.service;

import com.example.socialnetwork.user.event.UserRegisteredEvent;

public interface ProfileService {
  void createProfile(UserRegisteredEvent event);
}
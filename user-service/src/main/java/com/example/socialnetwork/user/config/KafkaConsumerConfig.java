package com.example.socialnetwork.user.config;

import com.example.socialnetwork.user.event.UserRegisteredEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
  @Bean
  public ConsumerFactory<String, UserRegisteredEvent> consumerFactory(KafkaProperties properties) {
    JacksonJsonDeserializer<UserRegisteredEvent> deserializer = new JacksonJsonDeserializer<>(UserRegisteredEvent.class);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);

    return new DefaultKafkaConsumerFactory<>(
      properties.buildConsumerProperties(),
      new StringDeserializer(),
      deserializer
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> kafkaListenerContainerFactory(
    ConsumerFactory<String, UserRegisteredEvent> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);

    return factory;
  }
}
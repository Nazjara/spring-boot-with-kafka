package com.nazjara;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@TestConfiguration
public class KafkaTestConfiguration {

  @Bean
  public RecordMessageConverter messageConverter() {
    return new StringJsonMessageConverter();
  }

  @Bean
  public KafkaTestListener kafkaTestListener() {
    return new KafkaTestListener();
  }
}

package com.nazjara.handler;

import com.nazjara.message.DispatchTracking;
import com.nazjara.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DispatchPreparingHandler {

  private final TrackingService trackingService;

  @KafkaListener(
      groupId = "tracking.dispatch.preparing.consumer",
      topics = "${kafka.topic.dispatch.tracking}",
      containerFactory = "kafkaListenerContainerFactory")
  public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key,
      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
      @Payload DispatchTracking payload) {
    log.info("Received message: {} with key {} from partition {}", payload, key, partition);

    trackingService.process(payload);
  }
}

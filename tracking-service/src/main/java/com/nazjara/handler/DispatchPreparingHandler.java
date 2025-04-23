package com.nazjara.handler;

import com.nazjara.exception.NotRetryableException;
import com.nazjara.exception.RetryableException;
import com.nazjara.message.DispatchTracking;
import com.nazjara.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(
    groupId = "tracking.dispatch.preparing.consumer",
    topics = {"${kafka.topic.dispatch.tracking}", "${kafka.topic.dispatch.tracking.dlt}"},
    containerFactory = "kafkaListenerContainerFactory")
public class DispatchPreparingHandler {

  private final TrackingService trackingService;

  @KafkaHandler
  public void listen(@Header(KafkaHeaders.RECEIVED_KEY) String key,
      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
      @Payload DispatchTracking payload) {
    log.info("Received message: {} with key {} from partition {}", payload, key, partition);

    try {
      trackingService.process(payload);
    } catch (RetryableException e) {
      log.warn(e.getMessage(), e);
      throw e;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new NotRetryableException();
    }
  }

  @KafkaHandler
  public void listenDlt(@Header(KafkaHeaders.RECEIVED_KEY) String key,
      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
      @Payload DispatchTracking payload) {
    log.info("Received message from dead letter topic: {} with key {} from partition {}", payload,
        key, partition);

    // some processing
  }
}

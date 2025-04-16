package com.nazjara.handler;

import com.nazjara.message.OrderCreated;
import com.nazjara.service.DispatchService;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedHandler {

  private final DispatchService dispatchService;

  @KafkaListener(
      groupId = "dispatch.order.created.consumer",
      topics = "${kafka.topic.order-created}",
      containerFactory = "kafkaListenerContainerFactory")
  public void listen(OrderCreated payload) {
    log.info("Received message: {}", payload);

    try {
      dispatchService.process(payload);
    } catch (ExecutionException | InterruptedException e) {
      log.error("OrderCreated event processing failed", e);
    }
  }
}

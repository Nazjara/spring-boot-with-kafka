package com.nazjara;

import com.nazjara.message.DispatchTracking;
import com.nazjara.message.OrderDispatched;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
public class KafkaTestListener {

  @Getter
  private final AtomicInteger dispatchPreparingCounter = new AtomicInteger(0);

  @Getter
  private final AtomicInteger orderDispatchedCounter = new AtomicInteger(0);

  @KafkaListener(groupId = "KafkaIntegrationTest", topics = "${kafka.topic.dispatch.tracking}")
  void receiveDispatchTracking(@Payload DispatchTracking payload) {
    log.info("Received DispatchTracking: {}", payload);
    dispatchPreparingCounter.incrementAndGet();
  }

  @KafkaListener(groupId = "KafkaIntegrationTest", topics = "${kafka.topic.order.dispatched}")
  void receiveOrderDispatched(@Payload OrderDispatched payload) {
    log.info("Received OrderDispatched: {}", payload);
    orderDispatchedCounter.incrementAndGet();
  }


}

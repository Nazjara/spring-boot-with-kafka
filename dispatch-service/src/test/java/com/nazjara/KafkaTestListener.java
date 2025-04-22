package com.nazjara;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import com.nazjara.message.DispatchTracking;
import com.nazjara.message.OrderDispatched;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
public class KafkaTestListener {

  @Getter
  private final AtomicInteger dispatchPreparingCounter = new AtomicInteger(0);

  @Getter
  private final AtomicInteger orderDispatchedCounter = new AtomicInteger(0);

  @KafkaListener(groupId = "KafkaIntegrationTest", topics = "${kafka.topic.dispatch.tracking}")
  void receiveDispatchTracking(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload DispatchTracking payload) {
    log.info("Received DispatchTracking: {} with key {}", payload, key);
    assertThat(key, notNullValue());
    dispatchPreparingCounter.incrementAndGet();
  }

  @KafkaListener(groupId = "KafkaIntegrationTest", topics = "${kafka.topic.order.dispatched}")
  void receiveOrderDispatched(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload OrderDispatched payload) {
    log.info("Received OrderDispatched: {} with key {}", payload, key);
    assertThat(key, notNullValue());
    orderDispatchedCounter.incrementAndGet();
  }


}

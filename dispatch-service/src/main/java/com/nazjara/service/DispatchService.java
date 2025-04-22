package com.nazjara.service;

import com.nazjara.message.DispatchTracking;
import com.nazjara.message.OrderDispatched;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchService {

  @Value("${kafka.topic.order.dispatched}")
  private String orderDispatchedTopic;

  @Value("${kafka.topic.dispatch.tracking}")
  private String dispatchTrackingTopic;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void process(UUID orderId) throws ExecutionException, InterruptedException {
    var dispatchPreparing = DispatchTracking.builder()
        .orderId(orderId)
        .build();

    // get() makes this call synchronous
    kafkaTemplate.send(dispatchTrackingTopic, String.valueOf(orderId), dispatchPreparing).get();

    var orderDispatched = OrderDispatched.builder()
        .orderId(orderId)
        .build();

    kafkaTemplate.send(orderDispatchedTopic, String.valueOf(orderId), orderDispatched);
  }
}

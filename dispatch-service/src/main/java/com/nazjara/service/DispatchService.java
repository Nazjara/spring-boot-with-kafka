package com.nazjara.service;

import com.nazjara.message.OrderCreated;
import com.nazjara.message.OrderDispatched;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DispatchService {

  @Value("${kafka.topic.order-dispatched}")
  private String topic;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void process(OrderCreated payload) throws ExecutionException, InterruptedException {
    var orderDispatched = OrderDispatched.builder()
        .orderId(payload.getOrderId())
        .build();

    // get() makes this call synchronous
    kafkaTemplate.send(topic, orderDispatched).get();
  }
}

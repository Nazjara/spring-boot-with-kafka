package com.nazjara.service;

import com.nazjara.message.DispatchPreparing;
import com.nazjara.message.Status;
import com.nazjara.message.TrackingStatusUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingService {

  @Value("${kafka.topic.tracking.status}")
  private String trackingStatusTopic;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void process(DispatchPreparing payload) {
    kafkaTemplate.send(trackingStatusTopic, TrackingStatusUpdated.builder()
        .orderId(payload.orderId())
        .status(Status.PREPARING)
        .build());
  }
}

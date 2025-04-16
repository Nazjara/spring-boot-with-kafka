package com.nazjara.rest;

import com.nazjara.message.OrderCreated;
import com.nazjara.service.DispatchService;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SimpleController {

  private final DispatchService dispatchService;

  @PostMapping("/publish")
  public void publishEvent(@RequestParam String item, @RequestParam UUID orderId) {
    try {
      dispatchService.process(OrderCreated.builder()
          .item(item)
          .orderId(orderId)
          .build());
    } catch (ExecutionException | InterruptedException e) {
      log.error("Event processing failed", e);
    }
  }
}

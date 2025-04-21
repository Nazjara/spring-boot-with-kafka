package com.nazjara;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(controlledShutdown = true,
    kraft = true,
    topics = {"${kafka.topic.dispatch.tracking}", "${kafka.topic.order.dispatched}"})
@AutoConfigureMockMvc
@Import(KafkaTestConfiguration.class)
public class OrderDispatchedIntegrationTest {

  @Autowired
  private KafkaTestListener kafkaTestListener;

  @Autowired
  private MockMvc mockMvc;

  private String item;
  private UUID orderId;

  @BeforeEach
  void setUp() {
    item = provideWithRandomString();
    orderId = UUID.randomUUID();

    kafkaTestListener.getOrderDispatchedCounter().set(0);
    kafkaTestListener.getDispatchPreparingCounter().set(0);
  }

  @Test
  public void orderDispatchedTest() throws Exception {
    // Wait for embedded Kafka partitions to be assigned
    // Maybe there's a better way to do this
    Thread.sleep(3000);

    mockMvc.perform(post("/api/publish")
            .param("item", item)
            .param("orderId", orderId.toString()))
        .andExpect(status().isOk());

    await().atMost(3, TimeUnit.SECONDS)
        .pollDelay(100, TimeUnit.MILLISECONDS)
        .until(kafkaTestListener.getOrderDispatchedCounter()::get, equalTo(1));

    await().atMost(3, TimeUnit.SECONDS)
        .pollDelay(100, TimeUnit.MILLISECONDS)
        .until(kafkaTestListener.getDispatchPreparingCounter()::get, equalTo(1));
  }

  private static String provideWithRandomString() {
    return new Random().ints(97, 123)
        .limit(10)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}

package com.recruitmentTask.demo.service;


import com.recruitmentTask.demo.dto.OrderEvent;
import com.recruitmentTask.demo.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private KafkaTemplate<String, OrderEvent> kafkaTemplate;

  @InjectMocks
  private OrderService orderService;

  private OrderRequest testRequest;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(orderService, "TOPIC", "order-events");

    testRequest = new OrderRequest();
    testRequest.setTrackingNumber("TEST123");
    testRequest.setRecipientEmail("test@example.com");
    testRequest.setRecipientCountryCode("PL");
    testRequest.setSenderCountryCode("DE");
    testRequest.setStatusCode(50);
  }

  @Test
  void shouldProcessOrderAndSendEventToKafka() {
    CompletableFuture<SendResult<String, OrderEvent>> future = CompletableFuture.completedFuture(null);
    when(kafkaTemplate.send(anyString(), anyString(), any(OrderEvent.class))).thenReturn(future);

    OrderEvent result = orderService.processOrder(testRequest);

    assertThat(result).isNotNull();
    assertThat(result.getTrackingNumber()).isEqualTo("TEST123");
    assertThat(result.getRecipientEmail()).isEqualTo("test@example.com");
    assertThat(result.getRecipientCountryCode()).isEqualTo("PL");
    assertThat(result.getSenderCountryCode()).isEqualTo("DE");
    assertThat(result.getStatusCode()).isEqualTo(50);
    assertThat(result.getEventTimestamp()).isNotNull();

    verify(kafkaTemplate).send(anyString(), anyString(), any(OrderEvent.class));
  }

  @Test
  void shouldLogErrorWhenKafkaSendFails() {
    CompletableFuture<SendResult<String, OrderEvent>> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(new RuntimeException("Kafka unavailable"));
    when(kafkaTemplate.send(anyString(), anyString(), any(OrderEvent.class))).thenReturn(failedFuture);

    orderService.processOrder(testRequest);

    verify(kafkaTemplate).send(anyString(), anyString(), any(OrderEvent.class));
  }
}

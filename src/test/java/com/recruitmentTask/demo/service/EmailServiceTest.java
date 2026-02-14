package com.recruitmentTask.demo.service;
import com.recruitmentTask.demo.dto.OrderEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @InjectMocks
  private EmailService emailService;

  @Test
  void shouldSendEmailNotificationWithoutException() {
    OrderEvent event = new OrderEvent();
    event.setTrackingNumber("TEST123");
    event.setRecipientEmail("test@example.com");
    event.setRecipientCountryCode("PL");
    event.setSenderCountryCode("DE");
    event.setStatusCode(50);
    event.setEventTimestamp(LocalDateTime.now());

    assertDoesNotThrow(() -> emailService.sendOrderNotification(event));
  }

  @Test
  void shouldHandleNullEvent() {
    assertDoesNotThrow(() -> emailService.sendOrderNotification(null));
  }
}

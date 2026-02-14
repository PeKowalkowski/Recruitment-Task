package com.recruitmentTask.demo.service;


import com.recruitmentTask.demo.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private static final Logger log = LoggerFactory.getLogger(EmailService.class);

  public void sendOrderNotification(OrderEvent event) {
    if (event == null) {
      log.warn("Received null OrderEvent, skipping email notification");
      return;
    }

    if (event.getRecipientEmail() == null || event.getRecipientEmail().isBlank()) {
      log.warn("OrderEvent has no recipient email, skipping notification for tracking: {}",
        event.getTrackingNumber());
      return;
    }

    log.info("[MOCK EMAIL] Sending notification to: {}", event.getRecipientEmail());
    log.info("Tracking Number: {}", event.getTrackingNumber());
    log.info("Status Code: {}", event.getStatusCode());
    log.info("Route: {} → {}", event.getSenderCountryCode(), event.getRecipientCountryCode());
    log.info("[MOCK EMAIL] Email sent successfully");
  }
}

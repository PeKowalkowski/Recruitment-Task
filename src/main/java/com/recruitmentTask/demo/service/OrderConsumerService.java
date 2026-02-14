package com.recruitmentTask.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentTask.demo.domain.OrderEventEntity;
import com.recruitmentTask.demo.dto.OrderEvent;
import com.recruitmentTask.demo.repository.OrderEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumerService {

  private static final Logger log = LoggerFactory.getLogger(OrderConsumerService.class);

  private final OrderEventRepository repository;
  private final ObjectMapper objectMapper;
  private final EmailService emailService;

  public OrderConsumerService(OrderEventRepository repository,
                              ObjectMapper objectMapper,
                              EmailService emailService) {
    this.repository = repository;
    this.objectMapper = objectMapper;
    this.emailService = emailService;
  }

  @KafkaListener(topics = "${cloudkarafka.topic:order-events}", groupId = "order-processors")
  public void consumeOrderEvent(OrderEvent event) {
    log.info("Received order event from Kafka: tracking={}, status={}",
      event.getTrackingNumber(),
      event.getStatusCode());

    try {
      emailService.sendOrderNotification(event);

      OrderEventEntity entity = convertToEntity(event);
      OrderEventEntity saved = repository.save(entity);

      log.info("Order event saved to database: id={}, tracking={}",
        saved.getId(),
        saved.getTrackingNumber());

    } catch (Exception e) {
      log.error("Failed to process order event: tracking={}",
        event.getTrackingNumber(), e);
      throw e;
    }
  }

  private OrderEventEntity convertToEntity(OrderEvent event) {
    String rawPayload;
    try {
      rawPayload = objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      log.warn("Failed to serialize event to JSON, using toString()", e);
      rawPayload = event.toString();
    }

    OrderEventEntity entity = new OrderEventEntity();
    entity.setTrackingNumber(event.getTrackingNumber());
    entity.setRecipientEmail(event.getRecipientEmail());
    entity.setRecipientCountryCode(event.getRecipientCountryCode());
    entity.setSenderCountryCode(event.getSenderCountryCode());
    entity.setStatusCode(event.getStatusCode());
    entity.setRawPayload(rawPayload);

    return entity;
  }
}

package com.recruitmentTask.demo.service;

import com.recruitmentTask.demo.dto.OrderEvent;
import com.recruitmentTask.demo.dto.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);

  @Value("${cloudkarafka.topic:order-events}")
  private String TOPIC;

  private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

  public OrderService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public OrderEvent processOrder(OrderRequest request) {
    log.info("Processing order request for tracking number: {}", request.getTrackingNumber());

    OrderEvent event = OrderEvent.fromRequest(request);

    kafkaTemplate.send(TOPIC, event.getTrackingNumber(), event)
      .whenComplete((result, ex) -> {
        if (ex == null) {
          log.info("Order event sent to Kafka: tracking={}, status={}, offset={}",
            event.getTrackingNumber(),
            event.getStatusCode(),
            result.getRecordMetadata().offset());
        } else {
          log.error("Failed to send order event to Kafka: tracking={}",
            event.getTrackingNumber(), ex);
        }
      });

    return event;
  }
}

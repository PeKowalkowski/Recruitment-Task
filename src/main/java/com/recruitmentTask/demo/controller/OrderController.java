package com.recruitmentTask.demo.controller;

import com.recruitmentTask.demo.dto.OrderEvent;
import com.recruitmentTask.demo.dto.OrderRequest;
import com.recruitmentTask.demo.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private static final Logger log = LoggerFactory.getLogger(OrderController.class);

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<OrderEvent> createOrder(@Valid @RequestBody OrderRequest request) {
    log.info("Received order request: tracking={}, status={}",
      request.getTrackingNumber(),
      request.getStatusCode());

    OrderEvent event = orderService.processOrder(request);

    log.info("Order accepted for processing: tracking={}", event.getTrackingNumber());

    return ResponseEntity
      .status(HttpStatus.ACCEPTED)
      .body(event);
  }
}

package com.recruitmentTask.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tracking_number", nullable = false)
  private String trackingNumber;

  @Column(name = "recipient_email", nullable = false)
  private String recipientEmail;

  @Column(name = "recipient_country_code", nullable = false, length = 2)
  private String recipientCountryCode;

  @Column(name = "sender_country_code", nullable = false, length = 2)
  private String senderCountryCode;

  @Column(name = "status_code", nullable = false)
  private Integer statusCode;

  @Column(name = "received_at", nullable = false)
  private LocalDateTime receivedAt;

  @Column(name = "raw_payload", nullable = false, columnDefinition = "text")
  private String rawPayload;

  @PrePersist
  protected void onCreate() {
    if (receivedAt == null) {
      receivedAt = LocalDateTime.now();
    }
  }
}

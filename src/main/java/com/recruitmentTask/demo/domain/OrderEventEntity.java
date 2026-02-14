package com.recruitmentTask.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_events")
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

  public OrderEventEntity() {
  }

  public OrderEventEntity(String trackingNumber, String recipientEmail, String recipientCountryCode,
                          String senderCountryCode, Integer statusCode, String rawPayload) {
    this.trackingNumber = trackingNumber;
    this.recipientEmail = recipientEmail;
    this.recipientCountryCode = recipientCountryCode;
    this.senderCountryCode = senderCountryCode;
    this.statusCode = statusCode;
    this.rawPayload = rawPayload;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTrackingNumber() {
    return trackingNumber;
  }

  public void setTrackingNumber(String trackingNumber) {
    this.trackingNumber = trackingNumber;
  }

  public String getRecipientEmail() {
    return recipientEmail;
  }

  public void setRecipientEmail(String recipientEmail) {
    this.recipientEmail = recipientEmail;
  }

  public String getRecipientCountryCode() {
    return recipientCountryCode;
  }

  public void setRecipientCountryCode(String recipientCountryCode) {
    this.recipientCountryCode = recipientCountryCode;
  }

  public String getSenderCountryCode() {
    return senderCountryCode;
  }

  public void setSenderCountryCode(String senderCountryCode) {
    this.senderCountryCode = senderCountryCode;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public LocalDateTime getReceivedAt() {
    return receivedAt;
  }

  public void setReceivedAt(LocalDateTime receivedAt) {
    this.receivedAt = receivedAt;
  }

  public String getRawPayload() {
    return rawPayload;
  }

  public void setRawPayload(String rawPayload) {
    this.rawPayload = rawPayload;
  }
}

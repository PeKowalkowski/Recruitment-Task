package com.recruitmentTask.demo.dto;

import java.time.LocalDateTime;

public class OrderEvent {

  private String trackingNumber;
  private String recipientEmail;
  private String recipientCountryCode;
  private String senderCountryCode;
  private Integer statusCode;
  private LocalDateTime eventTimestamp;

  public OrderEvent() {
  }

  public OrderEvent(String trackingNumber, String recipientEmail, String recipientCountryCode,
                    String senderCountryCode, Integer statusCode, LocalDateTime eventTimestamp) {
    this.trackingNumber = trackingNumber;
    this.recipientEmail = recipientEmail;
    this.recipientCountryCode = recipientCountryCode;
    this.senderCountryCode = senderCountryCode;
    this.statusCode = statusCode;
    this.eventTimestamp = eventTimestamp;
  }

  public static OrderEvent fromRequest(OrderRequest request) {
    return new OrderEvent(
      request.getTrackingNumber(),
      request.getRecipientEmail(),
      request.getRecipientCountryCode(),
      request.getSenderCountryCode(),
      request.getStatusCode(),
      LocalDateTime.now()
    );
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

  public LocalDateTime getEventTimestamp() {
    return eventTimestamp;
  }

  public void setEventTimestamp(LocalDateTime eventTimestamp) {
    this.eventTimestamp = eventTimestamp;
  }
}

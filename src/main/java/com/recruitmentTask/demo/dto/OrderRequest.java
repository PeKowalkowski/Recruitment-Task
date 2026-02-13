package com.recruitmentTask.demo.dto;


import jakarta.validation.constraints.*;


public class OrderRequest {

  @NotBlank(message = "Tracking number is required")
  private String trackingNumber;

  @NotBlank(message = "Recipient email is required")
  @Email(message = "Invalid email format")
  private String recipientEmail;

  @NotBlank(message = "Recipient country code is required")
  @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
  @Pattern(regexp = "[A-Z]{2}", message = "Country code must consist of 2 uppercase letters")
  private String recipientCountryCode;

  @NotBlank(message = "Sender country code is required")
  @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
  @Pattern(regexp = "[A-Z]{2}", message = "Country code must consist of 2 uppercase letters")
  private String senderCountryCode;

  @NotNull(message = "Status code is required")
  @Min(value = 0, message = "Status code must be >= 0")
  @Max(value = 100, message = "Status code must be <= 100")
  private Integer statusCode;

  public OrderRequest() {
  }

  public OrderRequest(String trackingNumber, String recipientEmail, String recipientCountryCode, String senderCountryCode, Integer statusCode) {
    this.trackingNumber = trackingNumber;
    this.recipientEmail = recipientEmail;
    this.recipientCountryCode = recipientCountryCode;
    this.senderCountryCode = senderCountryCode;
    this.statusCode = statusCode;
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
}

package com.recruitmentTask.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentTask.demo.dto.OrderEvent;
import com.recruitmentTask.demo.dto.OrderRequest;
import com.recruitmentTask.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldAcceptValidOrder() throws Exception {
        OrderRequest request = createValidRequest();
        OrderEvent event = createEventFromRequest(request);
        when(orderService.processOrder(any())).thenReturn(event);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.trackingNumber").value("TEST123"))
                .andExpect(jsonPath("$.recipientEmail").value("test@example.com"))
                .andExpect(jsonPath("$.statusCode").value(50));
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        OrderRequest request = createValidRequest();
        request.setRecipientEmail("invalid-email");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.recipientEmail").exists());
    }

    @Test
    void shouldRejectInvalidCountryCode() throws Exception {
        OrderRequest request = createValidRequest();
        request.setRecipientCountryCode("pl");  // lowercase

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.recipientCountryCode").exists());
    }

    @Test
    void shouldRejectInvalidStatusCode() throws Exception {
        OrderRequest request = createValidRequest();
        request.setStatusCode(150);  // > 100

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.statusCode").exists());
    }

    @Test
    void shouldRejectBlankTrackingNumber() throws Exception {
        OrderRequest request = createValidRequest();
        request.setTrackingNumber("");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.trackingNumber").exists());
    }

    private OrderRequest createValidRequest() {
        OrderRequest request = new OrderRequest();
        request.setTrackingNumber("TEST123");
        request.setRecipientEmail("test@example.com");
        request.setRecipientCountryCode("PL");
        request.setSenderCountryCode("DE");
        request.setStatusCode(50);
        return request;
    }

    private OrderEvent createEventFromRequest(OrderRequest request) {
        OrderEvent event = new OrderEvent();
        event.setTrackingNumber(request.getTrackingNumber());
        event.setRecipientEmail(request.getRecipientEmail());
        event.setRecipientCountryCode(request.getRecipientCountryCode());
        event.setSenderCountryCode(request.getSenderCountryCode());
        event.setStatusCode(request.getStatusCode());
        event.setEventTimestamp(LocalDateTime.now());
        return event;
    }
}

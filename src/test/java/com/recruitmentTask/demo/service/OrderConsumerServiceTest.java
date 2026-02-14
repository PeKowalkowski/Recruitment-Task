package com.recruitmentTask.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentTask.demo.domain.OrderEventEntity;
import com.recruitmentTask.demo.dto.OrderEvent;
import com.recruitmentTask.demo.repository.OrderEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderConsumerServiceTest {

    @Mock
    private OrderEventRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderConsumerService consumerService;

    private OrderEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new OrderEvent();
        testEvent.setTrackingNumber("TEST123");
        testEvent.setRecipientEmail("test@example.com");
        testEvent.setRecipientCountryCode("PL");
        testEvent.setSenderCountryCode("DE");
        testEvent.setStatusCode(50);
        testEvent.setEventTimestamp(LocalDateTime.now());
    }

    @Test
    void shouldConsumeEventAndSaveToDatabase() throws JsonProcessingException {
        String jsonPayload = "{\"trackingNumber\":\"TEST123\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(jsonPayload);

        OrderEventEntity savedEntity = new OrderEventEntity();
        savedEntity.setId(1L);
        savedEntity.setTrackingNumber("TEST123");
        when(repository.save(any(OrderEventEntity.class))).thenReturn(savedEntity);

        consumerService.consumeOrderEvent(testEvent);

        ArgumentCaptor<OrderEventEntity> entityCaptor = ArgumentCaptor.forClass(OrderEventEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        OrderEventEntity captured = entityCaptor.getValue();
        assertThat(captured.getTrackingNumber()).isEqualTo("TEST123");
        assertThat(captured.getRecipientEmail()).isEqualTo("test@example.com");
        assertThat(captured.getRecipientCountryCode()).isEqualTo("PL");
        assertThat(captured.getSenderCountryCode()).isEqualTo("DE");
        assertThat(captured.getStatusCode()).isEqualTo(50);
        assertThat(captured.getRawPayload()).isEqualTo(jsonPayload);
    }

    @Test
    void shouldHandleJsonProcessingException() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {});

        OrderEventEntity savedEntity = new OrderEventEntity();
        savedEntity.setId(1L);
        when(repository.save(any(OrderEventEntity.class))).thenReturn(savedEntity);

        consumerService.consumeOrderEvent(testEvent);

        ArgumentCaptor<OrderEventEntity> entityCaptor = ArgumentCaptor.forClass(OrderEventEntity.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        OrderEventEntity captured = entityCaptor.getValue();
        assertThat(captured.getTrackingNumber()).isEqualTo("TEST123");
        assertThat(captured.getRawPayload()).isNotNull();
    }
}

package com.example.artisan_coffee.publisher;

import com.example.artisan_coffee.dto.OrderFulfilledEventDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderFulfilledEventPublisher {

    private final KafkaTemplate<String, OrderFulfilledEventDTO> kafkaTemplate;
    private final String topic = "order-fulfilled-events";

    public OrderFulfilledEventPublisher(KafkaTemplate<String, OrderFulfilledEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderFulfilledEventDTO event) {
        kafkaTemplate.send(topic, event.getOrderId(), event);
        System.out.println("Published OrderFulfilledEvent for Order ID: " + event.getOrderId());
    }
}


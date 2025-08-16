package com.example.artisan_coffee.publisher;

import com.example.artisan_coffee.dto.OrderEventDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPlacedEventPublisher {

    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;
    private final String topic = "order-placed-events";

    public OrderPlacedEventPublisher(KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderEventDTO event) {
        kafkaTemplate.send(topic, event.getOrderId(), event);
        System.out.println("Published OrderPlacedEvent for Order ID: " + event.getOrderId());
    }
}


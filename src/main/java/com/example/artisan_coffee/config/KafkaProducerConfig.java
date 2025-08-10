package com.example.artisan_coffee.config;

import com.example.artisan_coffee.producer.OrderPlacedEventProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public OrderPlacedEventProducer orderPlacedEventProducer() {
        return new OrderPlacedEventProducer("localhost:9092", "order-placed-events");
    }
}

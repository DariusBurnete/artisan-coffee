package com.example.artisan_coffee.consumer;

import com.example.artisan_coffee.dto.OrderFulfilledEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderFulfilledEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final JavaMailSender mailSender;

    public OrderFulfilledEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "order-fulfilled-events", groupId = "order-confirmation-service")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            OrderFulfilledEventDTO event = objectMapper.readValue(record.value(), OrderFulfilledEventDTO.class);
            sendFulfilledEmail(event);
        } catch (Exception e) {
            System.err.println("Failed to process fulfilled order event: " + e.getMessage());
        }
    }

    private void sendFulfilledEmail(OrderFulfilledEventDTO event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getCustomerEmail());
        message.setSubject("Your Order Has Been Fulfilled - Order ID " + event.getOrderId());
        message.setText("Hello " + event.getCustomerName() + ",\n\n"
                + "Your order placed with Artisan Coffee has been fulfilled!\n"
                + "Fulfilled Date: " + event.getFulfilledDate() + "\n"
                + "We hope you enjoy your coffee!\n\n"
                + "Best regards,\n"
                + "Artisan Coffee Team");

        mailSender.send(message);
    }
}


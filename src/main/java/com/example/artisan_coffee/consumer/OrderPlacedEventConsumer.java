package com.example.artisan_coffee.consumer;

import com.example.artisan_coffee.dto.OrderEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderPlacedEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final JavaMailSender mailSender;

    public OrderPlacedEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "order-placed-events", groupId = "order-confirmation-service")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            OrderEventDTO event = objectMapper.readValue(record.value(), OrderEventDTO.class);

            System.out.println("Order confirmation received for Order ID: " + event.getOrderId());

            sendOrderConfirmationEmail(event);

        } catch (Exception e) {
            System.err.println("Failed to process order event: " + e.getMessage());
        }
    }

    private void sendOrderConfirmationEmail(OrderEventDTO event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getCustomerEmail());
        message.setSubject("Your Order Confirmation - Order ID " + event.getOrderId());
        message.setText("Hello " + event.getCustomerName() + ",\n\n"
                + "Thank you for your order!\n"
                + "Order ID: " + event.getOrderId() + "\n"
                + "Total: $" + event.getTotalPrice() + "\n"
                + "We will ship your order soon.\n\n"
                + "Best regards,\n"
                + "Artisan Coffee Team");

        mailSender.send(message);

        System.out.println("Email sent to: " + event.getCustomerEmail());
    }
}
package com.example.artisan_coffee.consumer;

import com.example.artisan_coffee.dto.OrderFulfilledEventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderFulfilledEventConsumer {

    private final JavaMailSender mailSender;

    public OrderFulfilledEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "order-fulfilled-events", groupId = "order-confirmation-service")
    public void consume(OrderFulfilledEventDTO event, Acknowledgment ack) {
        try {
            sendFulfilledEmail(event);
            ack.acknowledge();
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
        System.out.println("Fulfillment email sent to: " + event.getCustomerEmail());
    }
}

package com.example.artisan_coffee.consumer;

import com.example.artisan_coffee.dto.OrderEventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderPlacedEventConsumer {

    private final JavaMailSender mailSender;

    public OrderPlacedEventConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "order-placed-events", groupId = "order-confirmation-service")
    public void consume(OrderEventDTO event, Acknowledgment ack) {
        try {
            sendOrderConfirmationEmail(event);
            ack.acknowledge();
        } catch (Exception e) {
            System.err.println("Failed to process fulfilled order event: " + e.getMessage());
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

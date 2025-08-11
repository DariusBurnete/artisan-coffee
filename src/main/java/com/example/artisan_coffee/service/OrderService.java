package com.example.artisan_coffee.service;

import com.example.artisan_coffee.dto.CartItemDTO;
import com.example.artisan_coffee.dto.OrderDTO;
import com.example.artisan_coffee.dto.OrderEventDTO;
import com.example.artisan_coffee.dto.OrderFulfilledEventDTO;
import com.example.artisan_coffee.entity.*;
import com.example.artisan_coffee.mapper.OrderEventMapper;
import com.example.artisan_coffee.producer.OrderPlacedEventProducer;
import com.example.artisan_coffee.producer.OrderFulfilledEventProducer;
import com.example.artisan_coffee.repository.OrderRepository;
import com.example.artisan_coffee.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderPlacedEventProducer eventProducer;
    private final OrderFulfilledEventProducer orderFulfilledEventProducer;

    public OrderService(
            OrderRepository orderRepository, CartService cartService,
            UserRepository userRepository, OrderPlacedEventProducer eventProducer,
            OrderFulfilledEventProducer orderFulfilledEventProducer
    ) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.eventProducer = eventProducer;
        this.orderFulfilledEventProducer = orderFulfilledEventProducer;
    }

    public Order placeOrder(OrderDTO orderDTO, Authentication authentication, HttpSession session) {
        List<CartItemDTO> cartItems = cartService.getCartItems(session);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setCustomerName(orderDTO.getCustomerName());
        order.setAddress(new Address(
                orderDTO.getShippingAddress().getStreet(),
                orderDTO.getShippingAddress().getCity(),
                orderDTO.getShippingAddress().getPostalCode(),
                orderDTO.getShippingAddress().getCountry()
        ));
        order.setTotalPrice(cartService.getCartTotal(session));

        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> new OrderItem(
                        null,
                        ci.getProductId(),
                        ci.getName(),
                        ci.getPrice(),
                        ci.getQuantity(),
                        order
                ))
                .collect(Collectors.toList());

        order.setItems(orderItems);

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        } else {
            order.setUser(null);
        }

        order.setOrderDate(LocalDate.now());

        Order saved = orderRepository.save(order);

        cartService.clearCart(session);

        OrderEventDTO eventDTO = OrderEventMapper.toOrderEventDTO(saved);

        try {
            eventProducer.publishOrderPlacedEvent(eventDTO).get();
        } catch (Exception e) {
            System.err.println("Failed to publish order event: " + e.getMessage());
        }

        return saved;
    }

    public List<Order> getOrdersByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    public Order getOrderDetailsForUser(Long orderId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found or does not belong to user"));
    }

    public List<Order> getUnfulfilledOrders() {
        return orderRepository.findByFulfilledFalse();
    }

    public Order markOrderFulfilled(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setFulfilled(true);
        order.setFulfilledDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);

        try {
            OrderFulfilledEventDTO fulfilledEvent = new OrderFulfilledEventDTO(
                    savedOrder.getId().toString(),
                    savedOrder.getCustomerName(),
                    savedOrder.getUser() != null ? savedOrder.getUser().getEmail() : null,
                    savedOrder.getFulfilledDate()
            );
            orderFulfilledEventProducer.publishOrderFulfilledEvent(fulfilledEvent).get();
        } catch (Exception e) {
            System.err.println("Failed to publish fulfilled order event: " + e.getMessage());
        }
        return savedOrder;
    }

}


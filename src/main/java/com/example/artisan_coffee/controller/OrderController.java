package com.example.artisan_coffee.controller;

import com.example.artisan_coffee.entity.Order;
import com.example.artisan_coffee.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/my")
    @ResponseBody
    public List<Order> getOrdersByUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String email = authentication.getName();
        return orderService.getOrdersByUserEmail(email);
    }

    @GetMapping("/api/{orderId}")
    @ResponseBody
    public Order getOrderDetails(@PathVariable Long orderId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        String email = authentication.getName();
        return orderService.getOrderDetailsForUser(orderId, email);
    }

    @GetMapping
    public String ordersPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String email = authentication.getName();
        List<Order> orders = orderService.getOrdersByUserEmail(email);
        model.addAttribute("orders", orders);
        return "orders";
    }
}

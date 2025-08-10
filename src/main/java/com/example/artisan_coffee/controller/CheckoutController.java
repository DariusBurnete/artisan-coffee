package com.example.artisan_coffee.controller;

import com.example.artisan_coffee.dto.OrderDTO;
import com.example.artisan_coffee.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final OrderService orderService;

    public CheckoutController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String checkoutForm(Model model) {
        OrderDTO orderDTO = new OrderDTO();
        model.addAttribute("orderDTO", orderDTO);
        return "checkout";
    }

    @PostMapping
    public String placeOrder(@ModelAttribute OrderDTO orderDTO, Authentication authentication, HttpSession session, Model model) {
        try{
            orderService.placeOrder(orderDTO, authentication, session);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to place order: " + e.getMessage());
            return "checkout";
        }

    }
}


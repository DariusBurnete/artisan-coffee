package com.example.artisan_coffee.controller;

import com.example.artisan_coffee.dto.CartItemDTO;
import com.example.artisan_coffee.dto.ProductDTO;
import com.example.artisan_coffee.service.CartService;
import com.example.artisan_coffee.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        System.out.println("Adding product id " + id + " quantity " + quantity + " to cart");
        ProductDTO product = productService.getProductById(id);
        cartService.addToCart(product, quantity, session);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItemDTO> cartItems = cartService.getCartItems(session);
        System.out.println("Cart items in session: " + (cartItems == null ? 0 : cartItems.size()));
        model.addAttribute("items", cartItems);
        model.addAttribute("total", cartService.getCartTotal(session));
        return "cart";
    }

    @PostMapping("/update/{id}")
    public String updateCartItem(
            @PathVariable Long id,
            @RequestParam int quantity,
            HttpSession session
    ) {
        cartService.updateCartItem(id, quantity, session);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeFromCart(id, session);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}


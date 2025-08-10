package com.example.artisan_coffee.service;

import com.example.artisan_coffee.dto.CartItemDTO;
import com.example.artisan_coffee.dto.ProductDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    @SuppressWarnings("unchecked")
    public void addToCart(ProductDTO product, int quantity, HttpSession session) {
        System.out.println("Method addToCart called");

        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            System.out.println("Cart was null, created new cart");
        }

        Optional<CartItemDTO> existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            System.out.println("Increased quantity of existing item: " + product.getName());
        } else {
            cart.add(new CartItemDTO(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    quantity
            ));
            System.out.println("Added new item to cart: " + product.getName());
        }

        session.setAttribute(CART_SESSION_KEY, cart);
        System.out.println("Cart size after add: " + cart.size());
    }

    @SuppressWarnings("unchecked")
    public List<CartItemDTO> getCartItems(HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute(CART_SESSION_KEY);
        return cart != null ? cart : new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public void removeFromCart(Long productId, HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute(CART_SESSION_KEY);
        if (cart != null) {
            cart.removeIf(item -> item.getProductId().equals(productId));
            session.setAttribute(CART_SESSION_KEY, cart);
        }
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public double getCartTotal(HttpSession session) {
        return getCartItems(session).stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();
    }

    @SuppressWarnings("unchecked")
    public void updateCartItem(Long productId, int quantity, HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");
        if (cart != null) {
            cart.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
            session.setAttribute("cart", cart);
        }
    }

}

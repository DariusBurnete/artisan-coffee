package com.example.artisan_coffee.service;

import com.example.artisan_coffee.repository.ProductRepository;
import com.example.artisan_coffee.dto.CartItemDTO;
import com.example.artisan_coffee.dto.ProductDTO;
import com.example.artisan_coffee.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";
    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    public void addToCart(ProductDTO product, int quantity, HttpSession session) {
        System.out.println("Method addToCart called");

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            System.out.println("Cart was null, created new cart");
        }

        Optional<CartItemDTO> existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        int existingQuantity = existingItem.map(CartItemDTO::getQuantity).orElse(0);
        int newTotalQuantity = existingQuantity + quantity;

        if (newTotalQuantity > product.getQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            System.out.println("Increased quantity of existing item: " + product.getName());
        } else {
            cart.add(new CartItemDTO(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    quantity,
                    product.getQuantity()
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
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

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

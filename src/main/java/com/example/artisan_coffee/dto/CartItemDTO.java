package com.example.artisan_coffee.dto;

public class CartItemDTO {
    private final Long productId;
    private final String name;
    private final String imageUrl;
    private final double price;
    private int quantity;
    private final int stock;

    public CartItemDTO(Long productId, String name, String imageUrl, double price, int quantity, int stock) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public int getStock() {
        return stock;
    }
}


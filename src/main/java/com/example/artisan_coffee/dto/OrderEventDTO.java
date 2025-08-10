package com.example.artisan_coffee.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class OrderEventDTO {
    private String orderId;
    private String customerName;
    private double totalPrice;
    private LocalDate orderDate;
    private List<OrderItemDTO> items;
    private String customerEmail;

    public OrderEventDTO() {}

    @JsonCreator
    public OrderEventDTO(@JsonProperty("orderId") String orderId, @JsonProperty("customerName") String customerName,
                         @JsonProperty("customerEmail") String customerEmail, @JsonProperty("totalPrice") double totalPrice,
                         @JsonProperty("orderDate") LocalDate orderDate, @JsonProperty("items") List<OrderItemDTO> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public static class OrderItemDTO {
        private String productName;
        private int quantity;
        private double price;

        public OrderItemDTO() {}

        public OrderItemDTO(String productName, int quantity, double price) {
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}


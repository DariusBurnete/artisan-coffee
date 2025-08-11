package com.example.artisan_coffee.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private Long id;
    private String customerName;
    private AddressDTO shippingAddress;
    private List<CartItemDTO> items;
    private double totalPrice;

    private LocalDate orderDate;
    private boolean fulfilled;
    private LocalDate fulfilledDate;

    public OrderDTO() {}

    public OrderDTO(
            Long id, String customerName, AddressDTO shippingAddress,
            List<CartItemDTO> items, double totalPrice, LocalDate orderDate, boolean fulfilled,
            LocalDate fulfilledDate
    ) {
        this.id = id;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.fulfilled = fulfilled;
        this.fulfilledDate = fulfilledDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public AddressDTO getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressDTO shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public LocalDate getFulfilledDate() {
        return fulfilledDate;
    }

    public void setFulfilledDate(LocalDate fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
    }
}

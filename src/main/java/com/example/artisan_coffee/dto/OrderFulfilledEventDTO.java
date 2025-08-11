package com.example.artisan_coffee.dto;

import java.time.LocalDate;

public class OrderFulfilledEventDTO {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private LocalDate fulfilledDate;

    public OrderFulfilledEventDTO() {}

    public OrderFulfilledEventDTO(String orderId, String customerName, String customerEmail, LocalDate fulfilledDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.fulfilledDate = fulfilledDate;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public LocalDate getFulfilledDate() { return fulfilledDate; }
}
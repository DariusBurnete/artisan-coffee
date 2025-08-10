package com.example.artisan_coffee.mapper;

import com.example.artisan_coffee.dto.OrderEventDTO;
import com.example.artisan_coffee.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderEventMapper {

    public static OrderEventDTO toOrderEventDTO(Order order) {
        List<OrderEventDTO.OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderEventDTO.OrderItemDTO(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        String customerEmail = (order.getUser() != null) ? order.getUser().getEmail() : null;

        return new OrderEventDTO(
                order.getId().toString(),
                order.getCustomerName(),
                customerEmail,
                order.getTotalPrice(),
                order.getOrderDate(),
                itemDTOs
        );
    }
}

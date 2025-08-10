package com.example.artisan_coffee.repository;

import com.example.artisan_coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}


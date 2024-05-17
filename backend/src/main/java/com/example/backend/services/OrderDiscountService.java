package com.example.backend.services;

import com.example.backend.models.OrderDiscount;
import com.example.backend.repositories.OrderDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDiscountService {
    private final OrderDiscountRepository orderDiscountRepository;

    @Autowired
    public OrderDiscountService(OrderDiscountRepository orderDiscountRepository) {
        this.orderDiscountRepository = orderDiscountRepository;
    }

    public List<OrderDiscount> getAllOrderDiscounts() {
        return orderDiscountRepository.findAll();
    }
}

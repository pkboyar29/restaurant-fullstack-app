package com.example.backend.controllers;

import com.example.backend.models.OrderDiscount;
import com.example.backend.services.OrderDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5172")
@RestController
@RequestMapping(path = "api/order-discounts")
public class OrderDiscountController {
    private final OrderDiscountService orderDiscountService;

    @Autowired
    public OrderDiscountController(OrderDiscountService orderDiscountService) {
        this.orderDiscountService = orderDiscountService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDiscount>> getAllOrderDiscounts() {
        return ResponseEntity.status(HttpStatus.OK).body(orderDiscountService.getAllOrderDiscounts());
    }
}

package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "order_discounts")
public class OrderDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "required_number_orders", nullable = false)
    private int requiredNumberOrders;

    @Column(name = "discount", nullable = false)
    private int discount;

    public Long getId() {
        return id;
    }

    public int getRequiredNumberOrders() {
        return requiredNumberOrders;
    }

    public int getDiscount() {
        return discount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequiredNumberOrders(int requiredNumberOrders) {
        this.requiredNumberOrders = requiredNumberOrders;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

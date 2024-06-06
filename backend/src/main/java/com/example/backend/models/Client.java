package com.example.backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "number_orders", nullable = false)
    private int numberOrders;

    @ManyToOne
    @JoinColumn(name = "order_discount", nullable = false)
    private OrderDiscount orderDiscount;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getNumberOrders() {
        return numberOrders;
    }

    public OrderDiscount getOrderDiscount() {
        return orderDiscount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNumberOrders(int numberOrders) {
        this.numberOrders = numberOrders;
    }

    public void setOrderDiscount(OrderDiscount orderDiscount) {
        this.orderDiscount = orderDiscount;
    }
}

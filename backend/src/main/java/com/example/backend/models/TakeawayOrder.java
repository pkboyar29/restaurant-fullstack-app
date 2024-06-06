package com.example.backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "takeaway_orders")
public class TakeawayOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_phone", nullable = false)
    private String clientPhone;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "requirements", nullable = true)
    private String requirements;

    @Column(name = "cost", nullable = false)
    private int cost;

    @Column(name = "discounted_cost", nullable = false)
    private int discountedCost;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "receipt_date", nullable = false)
    private LocalDateTime receiptDate;

    @Column(name = "receipt_option", nullable = false)
    private String receiptOption;

    public Long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public User getUser() {
        return user;
    }

    public String getRequirements() {
        return requirements;
    }

    public int getCost() {
        return cost;
    }

    public int getDiscountedCost() {
        return discountedCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public String getReceiptOption() {
        return receiptOption;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDiscountedCost(int discountedCost) {
        this.discountedCost = discountedCost;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setReceiptOption(String receiptOption) {
        this.receiptOption = receiptOption;
    }
}

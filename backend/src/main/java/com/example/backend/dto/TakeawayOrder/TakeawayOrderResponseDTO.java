package com.example.backend.dto.TakeawayOrder;

import com.example.backend.models.TakeawayOrder;
import com.example.backend.models.TakeawayOrderPosition;

import java.time.LocalDateTime;
import java.util.List;

public class TakeawayOrderResponseDTO {
    private Long id;
    private int discountedCost;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private LocalDateTime receiptDate;
    private String receiptOption;
    private List<TakeawayOrderPosition> takeawayOrderPositions;

    public Long getId() {
        return id;
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

    public List<TakeawayOrderPosition> getTakeawayOrderPositions() {
        return takeawayOrderPositions;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTakeawayOrderPositions(List<TakeawayOrderPosition> takeawayOrderPositions) {
        this.takeawayOrderPositions = takeawayOrderPositions;
    }
}

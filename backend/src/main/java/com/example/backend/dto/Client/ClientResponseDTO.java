package com.example.backend.dto.Client;

import com.example.backend.models.OrderDiscount;

public class ClientResponseDTO {
    private Long id;
    private String username;
    private String firstName;
    private String phone;
    private OrderDiscount orderDiscount;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public OrderDiscount getOrderDiscount() {
        return orderDiscount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setOrderDiscount(OrderDiscount orderDiscount) {
        this.orderDiscount = orderDiscount;
    }
}

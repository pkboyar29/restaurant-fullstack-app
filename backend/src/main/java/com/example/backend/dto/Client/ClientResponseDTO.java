package com.example.backend.dto.Client;

import com.example.backend.models.OrderDiscount;

public class ClientResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phone;
    private String email;
    private OrderDiscount orderDiscount;
    private int numberOrders;

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public OrderDiscount getOrderDiscount() {
        return orderDiscount;
    }

    public int getNumberOrders() {
        return numberOrders;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrderDiscount(OrderDiscount orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public void setNumberOrders(int numberOrders) {
        this.numberOrders = numberOrders;
    }
}

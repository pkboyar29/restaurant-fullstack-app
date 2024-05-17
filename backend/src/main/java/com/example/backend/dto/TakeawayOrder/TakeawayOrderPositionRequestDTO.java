package com.example.backend.dto.TakeawayOrder;

public class TakeawayOrderPositionRequestDTO {
    private Long menuPositionId;
    private int number;
    private int totalPrice;

    public Long getMenuPositionId() {
        return menuPositionId;
    }

    public int getNumber() {
        return number;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setMenuPositionId(Long menuPositionId) {
        this.menuPositionId = menuPositionId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}

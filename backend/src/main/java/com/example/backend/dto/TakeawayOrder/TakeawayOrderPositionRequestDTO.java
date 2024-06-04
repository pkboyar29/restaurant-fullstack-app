package com.example.backend.dto.TakeawayOrder;

public class TakeawayOrderPositionRequestDTO {
    private Long menuPositionId;
    private int number;

    public Long getMenuPositionId() {
        return menuPositionId;
    }

    public int getNumber() {
        return number;
    }

    public void setMenuPositionId(Long menuPositionId) {
        this.menuPositionId = menuPositionId;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

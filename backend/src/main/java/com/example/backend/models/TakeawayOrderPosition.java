package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "takeaway_order_positions")
public class TakeawayOrderPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_position", nullable = false)
    MenuPosition menuPosition;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "takeaway_order", nullable = false)
    TakeawayOrder takeawayOrder;

    public Long getId() {
        return id;
    }

    public MenuPosition getMenuPosition() {
        return menuPosition;
    }

    public int getNumber() {
        return number;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public TakeawayOrder getTakeawayOrder() {
        return takeawayOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMenuPosition(MenuPosition menuPosition) {
        this.menuPosition = menuPosition;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTakeawayOrder(TakeawayOrder takeawayOrder) {
        this.takeawayOrder = takeawayOrder;
    }
}

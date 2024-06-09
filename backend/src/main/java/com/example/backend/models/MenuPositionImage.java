package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_position_images")
public class MenuPositionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "link", length = 200, nullable = false)
    private String link;

    @Column(name = "order_number", nullable = false)
    private int orderNumber;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "menu_position", nullable = false)
    private MenuPosition menuPosition;

    public Long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public MenuPosition getMenuPosition() {
        return menuPosition;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setMenuPosition(MenuPosition menuPosition) {
        this.menuPosition = menuPosition;
    }
}

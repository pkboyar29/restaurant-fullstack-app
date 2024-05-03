package com.example.backend.dto;

import com.example.backend.models.MenuSection;

public class MenuPositionRequestDTO {

    private String name;
    private String descr;
    private Long menuSection;
    private String portion;
    private int price;
    private boolean availability;

    public MenuPositionRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public Long getMenuSection() {
        return menuSection;
    }

    public String getPortion() {
        return portion;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public void setMenuSection(Long menuSection) {
        this.menuSection = menuSection;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}

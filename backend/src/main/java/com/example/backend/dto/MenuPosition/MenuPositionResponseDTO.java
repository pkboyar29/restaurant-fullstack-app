package com.example.backend.dto.MenuPosition;

import com.example.backend.models.MenuSection;

import java.time.LocalDate;

public class MenuPositionResponseDTO {

    private Long id;
    private String name;
    private String descr;
    private boolean availability;
    private LocalDate dateEnteredInMenu;
    private int price;
    private String portion;
    private MenuSection menuSection;
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public LocalDate getDateEnteredInMenu() {
        return dateEnteredInMenu;
    }

    public boolean isAvailability() {
        return availability;
    }

    public int getPrice() {
        return price;
    }

    public String getPortion() {
        return portion;
    }

    public MenuSection getMenuSection() {
        return menuSection;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setDateEnteredInMenu(LocalDate dateEnteredInMenu) {
        this.dateEnteredInMenu = dateEnteredInMenu;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public void setMenuSection(MenuSection menuSection) {
        this.menuSection = menuSection;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }
}

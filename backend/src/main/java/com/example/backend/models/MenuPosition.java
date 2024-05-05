package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "menu_positions")
public class MenuPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "descr", length = 400)
    private String descr;

    @Column(name = "availability", nullable = false)
    private Boolean availability = true;

    @Column(name = "date_entered_in_menu", nullable = false)
    private LocalDate dateEnteredInMenu;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "portion", length = 40, nullable = false)
    private String portion;

    @ManyToOne
    @JoinColumn(name = "menu_section", nullable = false)
    private MenuSection menuSection;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public LocalDate getDateEnteredInMenu() {
        return dateEnteredInMenu;
    }

    public int getPrice() {
        return price;
    }

    public String getPortion() {
        return portion;
    }

    @JsonIgnore
    public MenuSection getMenuSection() {
        return menuSection;
    }

    public String getSectionName() {
        return menuSection.getName();
    }

    // геттер, который будет возвращать список изображений для этой позиции меню, там где нет изображений, null?
    
    public void setName(String name) {
        this.name = name;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setAvailability(Boolean availability) {
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
}

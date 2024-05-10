package com.example.backend.dto.MenuPosition;

import org.springframework.web.multipart.MultipartFile;

public class MenuPositionRequestDTO {

    private String name;
    private String descr;
    private Long menuSection;
    private String portion;
    private int price;
    private boolean availability;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    private MultipartFile image4;

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

    public MultipartFile getImage1() {
        return image1;
    }

    public MultipartFile getImage2() {
        return image2;
    }

    public MultipartFile getImage3() {
        return image3;
    }

    public MultipartFile getImage4() {
        return image4;
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

    public void setImage1(MultipartFile image1) {
        this.image1 = image1;
    }

    public void setImage2(MultipartFile image2) {
        this.image2 = image2;
    }

    public void setImage3(MultipartFile image3) {
        this.image3 = image3;
    }

    public void setImage4(MultipartFile image4) {
        this.image4 = image4;
    }
}

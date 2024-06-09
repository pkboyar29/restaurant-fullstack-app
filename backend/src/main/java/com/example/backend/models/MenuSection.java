package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name="menu_sections")
public class MenuSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "descr")
    private String descr;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public MenuSection() { }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

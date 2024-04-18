package com.example.backend.repositories;

import com.example.backend.models.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, Long> {
    List<MenuSection> findAll();
}

package com.example.backend.repositories;

import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuPositionRepository extends JpaRepository<MenuPosition, Long> {
    List<MenuPosition> findByMenuSection(MenuSection menuSection);
}

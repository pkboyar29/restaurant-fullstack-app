package com.example.backend.repositories;

import com.example.backend.models.MenuPositionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuPositionImageRepository extends JpaRepository<MenuPositionImage, Long> {
    List<MenuPositionImage> findAll();
}

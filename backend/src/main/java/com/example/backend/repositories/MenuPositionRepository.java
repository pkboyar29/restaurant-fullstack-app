package com.example.backend.repositories;

import com.example.backend.models.MenuPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuPositionRepository extends JpaRepository<MenuPosition, Long> {
    List<MenuPosition> findAll();

    void deleteById(Long id);

    Optional<MenuPosition> findById(Long id);
}

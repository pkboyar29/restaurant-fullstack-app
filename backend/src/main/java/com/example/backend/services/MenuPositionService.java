package com.example.backend.services;

import com.example.backend.models.MenuPosition;
import com.example.backend.repositories.MenuPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuPositionService {
    private final MenuPositionRepository menuPositionRepository;

    @Autowired
    public MenuPositionService(MenuPositionRepository menuSectionRepository) {
        this.menuPositionRepository = menuSectionRepository;
    }

    public List<MenuPosition> getAllMenuPositions() {

        return menuPositionRepository.findAll();
    }
}

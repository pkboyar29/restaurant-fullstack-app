package com.example.backend.services;

import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuSection;
import com.example.backend.repositories.MenuPositionRepository;
import com.example.backend.repositories.MenuSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuPositionService {
    private final MenuPositionRepository menuPositionRepository;

    @Autowired
    public MenuPositionService(MenuPositionRepository menuPositionRepository) {
        this.menuPositionRepository = menuPositionRepository;
    }

    public List<MenuPosition> getAllMenuPositions() throws Exception {
        return menuPositionRepository.findAll();
    }
}

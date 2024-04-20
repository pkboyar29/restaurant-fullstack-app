package com.example.backend.services;

import com.example.backend.models.MenuPositionImage;
import com.example.backend.repositories.MenuPositionImageRepository;
import com.example.backend.repositories.MenuPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuPositionImageService {
    private final MenuPositionImageRepository menuPositionImageRepository;

    @Autowired
    public MenuPositionImageService(MenuPositionImageRepository menuPositionImageRepository) {
        this.menuPositionImageRepository = menuPositionImageRepository;
    }

    public List<MenuPositionImage> getAllMenuPositionImages() {
        return menuPositionImageRepository.findAll();
    }
}

package com.example.backend.services;

import com.example.backend.models.MenuSection;
import com.example.backend.repositories.MenuSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuSectionService {
    private final MenuSectionRepository menuSectionRepository;

    @Autowired
    public MenuSectionService(MenuSectionRepository menuSectionRepository) {
        this.menuSectionRepository = menuSectionRepository;
    }

    public List<MenuSection> getAllMenuSections() {
        return menuSectionRepository.findAll();
    }

    public void addMenuSection(MenuSection menuSectionDTO) {
        MenuSection newMenuSection = new MenuSection();
        newMenuSection.setName(menuSectionDTO.getName());
        newMenuSection.setDescr(menuSectionDTO.getDescr());

        try {
            menuSectionRepository.save(newMenuSection);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add menu section");
        }
    }
}

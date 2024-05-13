package com.example.backend.services;

import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuSection;
import com.example.backend.repositories.MenuSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void deleteMenuSection(Long id) {
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(id);
        if (optionalMenuSection.isEmpty()) {
            throw new ObjectNotFoundException("Menu section doesn't exist");
        }

        try {
            menuSectionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete menu section");
        }
    }

    public void updateMenuSection(Long id, MenuSection menuSectionDTO) {
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(id);
        if (optionalMenuSection.isEmpty()) {
            throw new ObjectNotFoundException("Menu section doesn't exist");
        }

        MenuSection updatedMenuSection = optionalMenuSection.get();
        updatedMenuSection.setName(menuSectionDTO.getName());
        updatedMenuSection.setDescr(menuSectionDTO.getDescr());

        try {
            menuSectionRepository.save(updatedMenuSection);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update menu section");
        }
    }
}

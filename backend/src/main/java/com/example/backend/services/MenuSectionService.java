package com.example.backend.services;

import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuPositionImage;
import com.example.backend.models.MenuSection;
import com.example.backend.repositories.MenuPositionImageRepository;
import com.example.backend.repositories.MenuPositionRepository;
import com.example.backend.repositories.MenuSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuSectionService {
    private final MenuSectionRepository menuSectionRepository;
    private final MenuPositionRepository menuPositionRepository;
    private final MenuPositionImageRepository menuPositionImageRepository;

    @Autowired
    public MenuSectionService(MenuSectionRepository menuSectionRepository, MenuPositionRepository menuPositionRepository, MenuPositionImageRepository menuPositionImageRepository) {
        this.menuSectionRepository = menuSectionRepository;
        this.menuPositionRepository = menuPositionRepository;
        this.menuPositionImageRepository = menuPositionImageRepository;
    }

    public List<MenuSection> getAllMenuSections() {
        List<MenuSection> menuSections = menuSectionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return menuSections.stream()
                .filter(menuSection -> !menuSection.isDeleted())
                .collect(Collectors.toList());
    }

    public MenuSection getMenuSectionById(Long sectionId) {
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(sectionId);
        if (optionalMenuSection.isEmpty()) {
            throw new ObjectNotFoundException("Menu section doesn't exist");
        }

        return optionalMenuSection.get();
    }

    public void addMenuSection(MenuSection menuSectionDTO) {
        MenuSection newMenuSection = new MenuSection();
        newMenuSection.setName(menuSectionDTO.getName());
        newMenuSection.setDescr(menuSectionDTO.getDescr());
        newMenuSection.setDeleted(false);

        try {
            menuSectionRepository.save(newMenuSection);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add menu section");
        }
    }

    public void deleteMenuSection(Long id) {
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(id);
        if (optionalMenuSection.isEmpty()) { throw new ObjectNotFoundException("Menu section doesn't exist"); }
        MenuSection menuSection = optionalMenuSection.get();

        try {
            menuSection.setDeleted(true);
            menuSectionRepository.save(menuSection);

            List<MenuPosition> menuPositions = menuPositionRepository.findByMenuSection(menuSection);
            for (MenuPosition menuPosition: menuPositions) {
                menuPosition.setDeleted(true);
                menuPositionRepository.save(menuPosition);
                List<MenuPositionImage> images = menuPositionImageRepository.findByMenuPosition(menuPosition);
                for (MenuPositionImage image: images) {
                    image.setDeleted(true);
                    menuPositionImageRepository.save(image);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete menu section");
        }
    }

    public void updateMenuSection(Long id, MenuSection menuSectionDTO) {
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(id);
        if (optionalMenuSection.isEmpty()) { throw new ObjectNotFoundException("Menu section doesn't exist"); }

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

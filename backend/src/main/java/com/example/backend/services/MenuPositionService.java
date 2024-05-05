package com.example.backend.services;

import com.example.backend.dto.MenuPositionRequestDTO;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuPositionImage;
import com.example.backend.models.MenuSection;
import com.example.backend.repositories.MenuPositionImageRepository;
import com.example.backend.repositories.MenuPositionRepository;
import com.example.backend.repositories.MenuSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MenuPositionService {
    private final MenuPositionRepository menuPositionRepository;
    private final MenuSectionRepository menuSectionRepository;
    private final MenuPositionImageRepository menuPositionImageRepository;

    @Autowired
    public MenuPositionService(MenuPositionRepository menuPositionRepository, MenuSectionRepository menuSectionRepository, MenuPositionImageRepository menuPositionImageRepository) {
        this.menuPositionRepository = menuPositionRepository;
        this.menuSectionRepository = menuSectionRepository;
        this.menuPositionImageRepository = menuPositionImageRepository;
    }

    public List<MenuPosition> getAllMenuPositions() {
        return menuPositionRepository.findAll();
    }

    public List<MenuPosition> getMenuPositionsBySectionId(Long sectionId) throws ObjectNotFoundException {

        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(sectionId);

        if (optionalMenuSection.isEmpty()) {
            throw new ObjectNotFoundException("Menu section doesn't exist");
        }

        MenuSection menuSection = optionalMenuSection.get();

        return menuPositionRepository.findByMenuSection(menuSection);
    }

    public MenuPosition getMenuPositionById(Long id) throws ObjectNotFoundException {
        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);

        if (optionalMenuPosition.isEmpty()) {
            throw new ObjectNotFoundException("Menu position doesn't exist");
        }

        return optionalMenuPosition.get();
    }

    public void addMenuPosition(MenuPositionRequestDTO menuPositionRequestDTO) {
        MenuPosition newMenuPosition = new MenuPosition();

        newMenuPosition.setName(menuPositionRequestDTO.getName());
        newMenuPosition.setDescr(menuPositionRequestDTO.getDescr());
        newMenuPosition.setPortion(menuPositionRequestDTO.getPortion());
        newMenuPosition.setPrice(menuPositionRequestDTO.getPrice());
        newMenuPosition.setAvailability(menuPositionRequestDTO.isAvailability());
        newMenuPosition.setDateEnteredInMenu(LocalDate.now());

        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(menuPositionRequestDTO.getMenuSection());
        if (optionalMenuSection.isEmpty()) {
            throw new ObjectNotFoundException("Menu section doesn't exist");
        }
        newMenuPosition.setMenuSection(optionalMenuSection.get());

        try {
            menuPositionRepository.save(newMenuPosition);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to add menu position: " + e.getMessage());
        }
    }

    public void updateMenuPosition(Long id, MenuPositionRequestDTO menuPositionRequestDTO) {

        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);
        if (optionalMenuPosition.isEmpty()) {
            throw new ObjectNotFoundException("Menu position doesn't exist");
        }

        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(menuPositionRequestDTO.getMenuSection());
        if (optionalMenuSection.isEmpty()) {
            throw new ObjectNotFoundException("Menu section doesn't exist");
        }

        MenuPosition updatedMenuPosition = optionalMenuPosition.get();
        updatedMenuPosition.setName(menuPositionRequestDTO.getName());
        updatedMenuPosition.setDescr(menuPositionRequestDTO.getDescr());
        updatedMenuPosition.setPortion(menuPositionRequestDTO.getPortion());
        updatedMenuPosition.setPrice(menuPositionRequestDTO.getPrice());
        updatedMenuPosition.setAvailability(menuPositionRequestDTO.isAvailability());
        updatedMenuPosition.setMenuSection(optionalMenuSection.get());

        try {
            menuPositionRepository.save(updatedMenuPosition);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to add menu position: " + e.getMessage());
        }
    }

    public void deleteMenuPosition(Long id) throws ObjectNotFoundException, IOException {
        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);
        if (optionalMenuPosition.isEmpty()) {
            throw new ObjectNotFoundException("Menu position doesn't exist");
        }

        MenuPosition menuPosition = optionalMenuPosition.get();
        List<MenuPositionImage> images = menuPositionImageRepository.findByMenuPosition(menuPosition);

//        for (MenuPositionImage image : images) {
//            String imagePath = "img/" + image.getLink();
//            System.out.println(imagePath);
//            Resource resource = new ClassPathResource(imagePath);
//
//            if (!resource.exists()) {
//                throw new IOException("Image " + image.getLink() + " doesn't exists");
//            }
//
//            URI uri = resource.getURI();
//            System.out.println(uri);
//            System.out.println(uri.getScheme());
//
//            Path path = Paths.get(uri);
//            Files.deleteIfExists(path);
//        }

        menuPositionImageRepository.deleteAllInBatch(images);

        menuPositionRepository.deleteById(id);
    }
}

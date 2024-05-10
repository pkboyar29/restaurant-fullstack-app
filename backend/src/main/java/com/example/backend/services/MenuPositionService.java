package com.example.backend.services;

import com.example.backend.dto.MenuPosition.MenuPositionRequestDTO;
import com.example.backend.dto.MenuPosition.MenuPositionResponseDTO;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuPositionImage;
import com.example.backend.models.MenuSection;
import com.example.backend.repositories.MenuPositionImageRepository;
import com.example.backend.repositories.MenuPositionRepository;
import com.example.backend.repositories.MenuSectionRepository;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final String imagesPath;

    @Autowired
    public MenuPositionService(MenuPositionRepository menuPositionRepository, MenuSectionRepository menuSectionRepository,
                               MenuPositionImageRepository menuPositionImageRepository, @Value("${images.path}") String imagesPath) {
        this.menuPositionRepository = menuPositionRepository;
        this.menuSectionRepository = menuSectionRepository;
        this.menuPositionImageRepository = menuPositionImageRepository;
        this.imagesPath = imagesPath;
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

    public MenuPositionResponseDTO getMenuPositionById(Long id) throws ObjectNotFoundException {

        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);
        if (optionalMenuPosition.isEmpty()) {
            throw new ObjectNotFoundException("Menu position doesn't exist");
        }
        MenuPosition menuPosition = optionalMenuPosition.get();

        MenuPositionResponseDTO menuPositionResponseDTO = new MenuPositionResponseDTO();
        menuPositionResponseDTO.setId(menuPosition.getId());
        menuPositionResponseDTO.setName(menuPosition.getName());
        menuPositionResponseDTO.setDescr(menuPosition.getDescr());
        menuPositionResponseDTO.setPortion(menuPosition.getPortion());
        menuPositionResponseDTO.setMenuSection(menuPosition.getMenuSection());
        menuPositionResponseDTO.setPrice(menuPosition.getPrice());
        menuPositionResponseDTO.setDateEnteredInMenu(menuPosition.getDateEnteredInMenu());
        menuPositionResponseDTO.setAvailability(menuPosition.getAvailability());

        List<MenuPositionImage> menuPositionImageList = menuPositionImageRepository.findByMenuPosition(menuPosition);
        for (MenuPositionImage image : menuPositionImageList) {
            switch (image.getOrderNumber()) {
                case 1:
                    menuPositionResponseDTO.setImage1(image.getLink());
                    break;
                case 2:
                    menuPositionResponseDTO.setImage2(image.getLink());
                    break;
                case 3:
                    menuPositionResponseDTO.setImage3(image.getLink());
                    break;
                case 4:
                    menuPositionResponseDTO.setImage4(image.getLink());
                    break;
                default:
                    System.out.println("hello world");
            }
        }

        return menuPositionResponseDTO;
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
            MenuPosition menuPositionWithId = menuPositionRepository.save(newMenuPosition);
            Long menuPositionId = menuPositionWithId.getId();

            createMenuPositionDirectory(menuPositionId);

            // добавляем изображения в директорию и БД
            Path menuPositionResourcePath = Paths.get(imagesPath + menuPositionId);

            MultipartFile imageFile1 = menuPositionRequestDTO.getImage1();
            MultipartFile imageFile2 = menuPositionRequestDTO.getImage2();
            MultipartFile imageFile3 = menuPositionRequestDTO.getImage3();
            MultipartFile imageFile4 = menuPositionRequestDTO.getImage4();

            if (imageFile1 != null && !imageFile1.isEmpty()) {
                Path image1Path = menuPositionResourcePath.resolve(imageFile1.getOriginalFilename());
                Files.copy(imageFile1.getInputStream(), image1Path);

                MenuPositionImage image1 = new MenuPositionImage();
                image1.setLink("/menu-position-images/" + menuPositionId + "/" + imageFile1.getOriginalFilename());
                image1.setOrderNumber(1);
                image1.setMenuPosition(menuPositionWithId);

                menuPositionImageRepository.save(image1);
            }

            if (imageFile2 != null && !imageFile2.isEmpty()) {
                Path image2Path = menuPositionResourcePath.resolve(imageFile2.getOriginalFilename());
                Files.copy(imageFile2.getInputStream(), image2Path);

                MenuPositionImage image2 = new MenuPositionImage();
                image2.setLink("/menu-position-images/" + menuPositionId + "/" + imageFile2.getOriginalFilename());
                image2.setOrderNumber(2);
                image2.setMenuPosition(menuPositionWithId);

                menuPositionImageRepository.save(image2);
            }

            if (imageFile3 != null && !imageFile3.isEmpty()) {
                Path image3Path = menuPositionResourcePath.resolve(imageFile3.getOriginalFilename());
                Files.copy(imageFile3.getInputStream(), image3Path);

                MenuPositionImage image3 = new MenuPositionImage();
                image3.setLink("/menu-position-images/" + menuPositionId + "/" + imageFile3.getOriginalFilename());
                image3.setOrderNumber(3);
                image3.setMenuPosition(menuPositionWithId);

                menuPositionImageRepository.save(image3);
            }

            if (imageFile4 != null && !imageFile4.isEmpty()) {
                Path image4Path = menuPositionResourcePath.resolve(imageFile4.getOriginalFilename());
                Files.copy(imageFile4.getInputStream(), image4Path);

                MenuPositionImage image4 = new MenuPositionImage();
                image4.setLink("/menu-position-images/" + menuPositionId + "/" + imageFile4.getOriginalFilename());
                image4.setOrderNumber(4);
                image4.setMenuPosition(menuPositionWithId);

                menuPositionImageRepository.save(image4);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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
            MenuPosition menuPositionWithId = menuPositionRepository.save(updatedMenuPosition);
            Long menuPositionId = menuPositionWithId.getId();

            // если директории еще не существует, то создаем ее
            createMenuPositionDirectory(menuPositionId);
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

    public void createMenuPositionDirectory(Long menuPositionId) {
        try {
            // получаем путь к директории ресурсов
            Path resourcePath = Paths.get(imagesPath);
            System.out.println(resourcePath);

            // создаем новую директорию
            Path newFolderPath = resourcePath.resolve(String.valueOf(menuPositionId));
            if (!Files.exists(newFolderPath)) {
                Files.createDirectory(newFolderPath);
            }
            else {
                System.out.println("Папка уже существует");
            }
        }
        catch (Exception e) {
            System.out.println("Ошибка при создании папки: " + e.getMessage());
        }
    }
}

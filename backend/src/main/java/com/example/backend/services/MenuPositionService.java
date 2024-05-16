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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final String imagesPathAdmin;
    private final String imagesPathClient;

    @Autowired
    public MenuPositionService(MenuPositionRepository menuPositionRepository, MenuSectionRepository menuSectionRepository,
                               MenuPositionImageRepository menuPositionImageRepository,
                               @Value("${images.path1}") String imagesPathAdmin, @Value("${images.path2}") String imagesPathClient) {
        this.menuPositionRepository = menuPositionRepository;
        this.menuSectionRepository = menuSectionRepository;
        this.menuPositionImageRepository = menuPositionImageRepository;
        this.imagesPathAdmin = imagesPathAdmin;
        this.imagesPathClient = imagesPathClient;
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

            // создаем директорию и добавляем туда изображения
            createMenuPositionDirectory(imagesPathAdmin, menuPositionId);
            createMenuPositionDirectory(imagesPathClient, menuPositionId);
            Path menuPositionAdminResourcePath = Paths.get(imagesPathAdmin + menuPositionId);
            Path menuPositionClientResourcePath = Paths.get(imagesPathClient + menuPositionId);
            addFilesToMenuPositionDirectory(menuPositionAdminResourcePath, menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4());
            addFilesToMenuPositionDirectory(menuPositionClientResourcePath, menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4());

            // добавляем изображения в БД
            addMenuPositionImages(menuPositionWithId, menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4());
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

            // создаем директорию (если ее нету), очищаем ее и добавляем туда изображения
            Path menuPositionAdminResourcePath = Paths.get(imagesPathAdmin + menuPositionId);
            Path menuPositionClientResourcePath = Paths.get(imagesPathClient + menuPositionId);

            deleteMenuPositionDirectory(menuPositionAdminResourcePath);
            deleteMenuPositionDirectory(menuPositionClientResourcePath);

            createMenuPositionDirectory(imagesPathAdmin, menuPositionId);
            createMenuPositionDirectory(imagesPathClient, menuPositionId);

            addFilesToMenuPositionDirectory(menuPositionAdminResourcePath, menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4());
            addFilesToMenuPositionDirectory(menuPositionClientResourcePath, menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4());

            // удаляем все изображения из БД
            List<MenuPositionImage> images = menuPositionImageRepository.findByMenuPosition(menuPositionWithId);
            menuPositionImageRepository.deleteAllInBatch(images);
            // добавляем их заново в БД
            addMenuPositionImages(menuPositionWithId, menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to add menu position: " + e.getMessage());
        }
    }

    public void deleteMenuPosition(Long id) {
        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);
        if (optionalMenuPosition.isEmpty()) {
            throw new ObjectNotFoundException("Menu position doesn't exist");
        }

        MenuPosition menuPosition = optionalMenuPosition.get();
        try {
            Path menuPositionAdminResourcePath = Paths.get(imagesPathAdmin + menuPosition.getId());
            deleteMenuPositionDirectory(menuPositionAdminResourcePath);

            Path menuPositionClientResourcePath = Paths.get(imagesPathClient + menuPosition.getId());
            deleteMenuPositionDirectory(menuPositionClientResourcePath);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        List<MenuPositionImage> images = menuPositionImageRepository.findByMenuPosition(menuPosition);
        try {
            menuPositionImageRepository.deleteAllInBatch(images);
            menuPositionRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createMenuPositionDirectory(String imagesPath, Long menuPositionId) {
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

    public void addFilesToMenuPositionDirectory(Path menuPositionDirectoryPath, MultipartFile image1, MultipartFile image2,
                                                MultipartFile image3, MultipartFile image4) throws IOException {
        if (image1 != null && !image1.isEmpty()) {
            Path image1Path = menuPositionDirectoryPath.resolve(image1.getOriginalFilename());
            Files.copy(image1.getInputStream(), image1Path);
        }
        if (image2 != null && !image2.isEmpty()) {
            Path image2Path = menuPositionDirectoryPath.resolve(image2.getOriginalFilename());
            Files.copy(image2.getInputStream(), image2Path);
        }
        if (image3 != null && !image3.isEmpty()) {
            Path image3Path = menuPositionDirectoryPath.resolve(image3.getOriginalFilename());
            Files.copy(image3.getInputStream(), image3Path);
        }
        if (image4 != null && !image4.isEmpty()) {
            Path image4Path = menuPositionDirectoryPath.resolve(image4.getOriginalFilename());
            Files.copy(image4.getInputStream(), image4Path);
        }
    }

    public void deleteMenuPositionDirectory(Path menuPositionDirectoryPath) throws IOException {
        Files.walk(menuPositionDirectoryPath)
                .sorted((p1, p2) -> -p1.compareTo(p2))
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        System.err.println("Не удалось удалить файл: " + p);
                        e.printStackTrace();
                    }
                });

        System.out.println("Папка успешно удалена: " + menuPositionDirectoryPath);
    }

    public void addMenuPositionImages(MenuPosition menuPosition, MultipartFile image1, MultipartFile image2,
                              MultipartFile image3, MultipartFile image4) {
        if (image1 != null && !image1.isEmpty()) {
            MenuPositionImage menuPositionImage1 = new MenuPositionImage();
            menuPositionImage1.setLink("/menu-position-images/" + menuPosition.getId() + "/" + image1.getOriginalFilename());
            menuPositionImage1.setOrderNumber(1);
            menuPositionImage1.setMenuPosition(menuPosition);

            menuPositionImageRepository.save(menuPositionImage1);
        }
        if (image2 != null && !image2.isEmpty()) {
            MenuPositionImage menuPositionImage2 = new MenuPositionImage();
            menuPositionImage2.setLink("/menu-position-images/" + menuPosition.getId() + "/" + image2.getOriginalFilename());
            menuPositionImage2.setOrderNumber(2);
            menuPositionImage2.setMenuPosition(menuPosition);

            menuPositionImageRepository.save(menuPositionImage2);
        }
        if (image3 != null && !image3.isEmpty()) {
            MenuPositionImage menuPositionImage3 = new MenuPositionImage();
            menuPositionImage3.setLink("/menu-position-images/" + menuPosition.getId() + "/" + image3.getOriginalFilename());
            menuPositionImage3.setOrderNumber(3);
            menuPositionImage3.setMenuPosition(menuPosition);

            menuPositionImageRepository.save(menuPositionImage3);
        }
        if (image4 != null && !image4.isEmpty()) {
            MenuPositionImage menuPositionImage4 = new MenuPositionImage();
            menuPositionImage4.setLink("/menu-position-images/" + menuPosition.getId() + "/" + image4.getOriginalFilename());
            menuPositionImage4.setOrderNumber(4);
            menuPositionImage4.setMenuPosition(menuPosition);

            menuPositionImageRepository.save(menuPositionImage4);
        }
    }
}

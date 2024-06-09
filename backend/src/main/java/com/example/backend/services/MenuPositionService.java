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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<MenuPositionResponseDTO> getAllMenuPositions() {
        List<MenuPosition> menuPositions = menuPositionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<MenuPosition> filteredMenuPositions = menuPositions.stream()
                .filter(menuPosition -> !menuPosition.isDeleted())
                .toList();
        return createListMenuPositionResponseDTO(filteredMenuPositions);
    }

    public List<MenuPositionResponseDTO> getAvailableMenuPositions() {
        List<MenuPositionResponseDTO> allMenuPositions = getAllMenuPositions();
        List<MenuPositionResponseDTO> availableMenuPositions = allMenuPositions.stream().
                                                    filter(MenuPositionResponseDTO::isAvailability).
                                                    collect(Collectors.toList());
        return availableMenuPositions;
    }

    public List<MenuPositionResponseDTO> getMenuPositionsBySectionId(Long sectionId) {
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(sectionId);
        if (optionalMenuSection.isEmpty()) { throw new ObjectNotFoundException("Menu section doesn't exist"); }
        MenuSection menuSection = optionalMenuSection.get();

        List<MenuPosition> menuPositions = menuPositionRepository.findByMenuSection(menuSection);
        List<MenuPosition> filteredMenuPositions = menuPositions.stream()
                .filter(menuPosition -> !menuPosition.isDeleted())
                .toList();
        return createListMenuPositionResponseDTO(filteredMenuPositions);
    }

    public List<MenuPositionResponseDTO> getAvailableMenuPositionsBySectionId(Long sectionId) {
        List<MenuPositionResponseDTO> allMenuPositions = getMenuPositionsBySectionId(sectionId);
        List<MenuPositionResponseDTO> availableMenuPositions = allMenuPositions.stream().
                                                                        filter(MenuPositionResponseDTO::isAvailability).
                                                                        collect(Collectors.toList());
        return availableMenuPositions;
    }

    public MenuPositionResponseDTO getMenuPositionById(Long id) throws ObjectNotFoundException {
        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);
        if (optionalMenuPosition.isEmpty()) { throw new ObjectNotFoundException("Menu position doesn't exist"); }
        MenuPosition menuPosition = optionalMenuPosition.get();

        return createMenuPositionResponseDTO(menuPosition);
    }

    public List<MenuPositionResponseDTO> createListMenuPositionResponseDTO(List<MenuPosition> menuPositionList) {
        List<MenuPositionResponseDTO> menuPositionResponseDTOList = new ArrayList<>();
        for (MenuPosition menuPosition : menuPositionList) {
            menuPositionResponseDTOList.add(createMenuPositionResponseDTO(menuPosition));
        }
        return menuPositionResponseDTOList;
    }

    public MenuPositionResponseDTO createMenuPositionResponseDTO(MenuPosition menuPosition) {
        MenuPositionResponseDTO menuPositionResponseDTO = new MenuPositionResponseDTO();
        menuPositionResponseDTO.setId(menuPosition.getId());
        menuPositionResponseDTO.setName(menuPosition.getName());
        menuPositionResponseDTO.setDescr(menuPosition.getDescr());
        menuPositionResponseDTO.setPortion(menuPosition.getPortion());
        menuPositionResponseDTO.setMenuSection(menuPosition.getMenuSection());
        menuPositionResponseDTO.setPrice(menuPosition.getPrice());
        menuPositionResponseDTO.setDateEnteredInMenu(menuPosition.getDateEnteredInMenu());
        menuPositionResponseDTO.setAvailability(menuPosition.getAvailability());
        menuPositionResponseDTO = addImagesToMenuPositionResponseDTO(menuPositionResponseDTO);

        return menuPositionResponseDTO;
    }

    public MenuPositionResponseDTO addImagesToMenuPositionResponseDTO(MenuPositionResponseDTO menuPositionResponseDTO) {
        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(menuPositionResponseDTO.getId());
        MenuPosition menuPosition = optionalMenuPosition.get();
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
        newMenuPosition.setDeleted(false);

        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(menuPositionRequestDTO.getMenuSection());
        if (optionalMenuSection.isEmpty()) { throw new ObjectNotFoundException("Menu section doesn't exist"); }
        newMenuPosition.setMenuSection(optionalMenuSection.get());

        try {
            MenuPosition menuPositionWithId = menuPositionRepository.save(newMenuPosition);
            Long menuPositionId = menuPositionWithId.getId();

            // создаем директорию и добавляем туда изображения
            createMenuPositionDirectory(imagesPathAdmin, menuPositionId);
            createMenuPositionDirectory(imagesPathClient, menuPositionId);
            Path menuPositionAdminResourcePath = Paths.get(imagesPathAdmin + menuPositionId);
            Path menuPositionClientResourcePath = Paths.get(imagesPathClient + menuPositionId);

            List<MultipartFile> images = Arrays.asList(menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4())
                    .stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());
            List<String> fileNames = generateUniqueFileNames(images);
            System.out.println("filenames: ");
            for (String fileName: fileNames) {
                System.out.println(fileName);
            }

            addFilesToMenuPositionDirectory(menuPositionAdminResourcePath, images, fileNames);
            addFilesToMenuPositionDirectory(menuPositionClientResourcePath, images, fileNames);

            // добавляем изображения в БД
            addMenuPositionImagesToDB(menuPositionWithId, fileNames);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to add menu position: " + e.getMessage());
        }
    }

    public void updateMenuPosition(Long id, MenuPositionRequestDTO menuPositionRequestDTO) {
        Optional<MenuPosition> optionalMenuPosition = menuPositionRepository.findById(id);
        if (optionalMenuPosition.isEmpty()) { throw new ObjectNotFoundException("Menu position doesn't exist"); }
        Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(menuPositionRequestDTO.getMenuSection());
        if (optionalMenuSection.isEmpty()) { throw new ObjectNotFoundException("Menu section doesn't exist"); }

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

            List<MultipartFile> images = Arrays.asList(menuPositionRequestDTO.getImage1(), menuPositionRequestDTO.getImage2(),
                    menuPositionRequestDTO.getImage3(), menuPositionRequestDTO.getImage4())
                    .stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());
            List<String> fileNames = generateUniqueFileNames(images);
            System.out.println("длина images " + images.size());
            System.out.println("длина filenames " + fileNames.size());
            System.out.println("filenames: ");
            for (String fileName: fileNames) {
                System.out.println(fileName);
            }

            addFilesToMenuPositionDirectory(menuPositionAdminResourcePath, images, fileNames);
            addFilesToMenuPositionDirectory(menuPositionClientResourcePath, images, fileNames);

            // удаляем все изображения из БД
            List<MenuPositionImage> menuPositionImages = menuPositionImageRepository.findByMenuPosition(menuPositionWithId);
            menuPositionImageRepository.deleteAllInBatch(menuPositionImages);
            // добавляем их заново в БД
            addMenuPositionImagesToDB(menuPositionWithId, fileNames);
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
        List<MenuPositionImage> images = menuPositionImageRepository.findByMenuPosition(menuPosition);
        try {
            menuPosition.setDeleted(true);
            menuPositionRepository.save(menuPosition);
            for (MenuPositionImage image: images) {
                image.setDeleted(true);
                menuPositionImageRepository.save(image);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createMenuPositionDirectory(String imagesPath, Long menuPositionId) {
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

    private List<String> generateUniqueFileNames(List<MultipartFile> images) {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
                String extension = StringUtils.getFilenameExtension(originalFilename);
                String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
                fileNames.add(uniqueFileName);
            }
        }
        return fileNames;
    }

    private void addFilesToMenuPositionDirectory(Path menuPositionDirectoryPath, List<MultipartFile> images, List<String> fileNames) throws IOException {
        System.out.println("добавляем изображения в директорию и перед добавлением печатаем fileName: ");
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            if (image != null && !image.isEmpty()) {
                String fileName = fileNames.get(i);
                System.out.println(fileName);
                Path imagePath = menuPositionDirectoryPath.resolve(fileName);
                Files.copy(image.getInputStream(), imagePath);
            }
        }
    }

    public void addMenuPositionImagesToDB(MenuPosition menuPosition, List<String> fileNames) {
        System.out.println("добавляем изображения в БД и перед добавлением печатаем fileName: ");
        for (int i = 0; i < fileNames.size(); i++) {
            System.out.println(fileNames.get(i));
            MenuPositionImage menuPositionImage = new MenuPositionImage();
            menuPositionImage.setMenuPosition(menuPosition);
            menuPositionImage.setLink("/menu-position-images/" + menuPosition.getId() + "/" + fileNames.get(i));
            menuPositionImage.setOrderNumber(i + 1);
            menuPositionImage.setDeleted(false);

            menuPositionImageRepository.save(menuPositionImage);
        }
    }
}

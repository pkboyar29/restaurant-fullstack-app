package com.example.backend.controllers;

import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuPositionImage;
import com.example.backend.services.MenuPositionImageService;
import com.example.backend.services.MenuPositionService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(path = "/api/menu-position-images")
public class MenuPositionImageController {
    private final MenuPositionImageService menuPositionImageService;

    @Autowired
    public MenuPositionImageController(MenuPositionImageService menuPositionImageService) {
        this.menuPositionImageService = menuPositionImageService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<MenuPositionImage>> getAllMenuPositionImages() {
        List<MenuPositionImage> allMenuPositionImages = menuPositionImageService.getAllMenuPositionImages();

        return ResponseEntity.status(HttpStatus.OK).body(allMenuPositionImages);
    }

    @GetMapping(path = "/{imageName}")
    public ResponseEntity<byte[]> getMenuPositionImage(@PathVariable String imageName) throws IOException {

        String imagePath = "img/" + imageName;
        Resource resource = new ClassPathResource(imagePath);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path path = Paths.get(resource.getURI());
        byte[] imageBytes = Files.readAllBytes(path);

        // Вернуть изображение как массив байт с нужным MIME типом
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }
}

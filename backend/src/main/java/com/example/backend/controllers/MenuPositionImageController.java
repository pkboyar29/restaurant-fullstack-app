package com.example.backend.controllers;

import com.example.backend.models.MenuPosition;
import com.example.backend.models.MenuPositionImage;
import com.example.backend.services.MenuPositionImageService;
import com.example.backend.services.MenuPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.example.backend.controllers;

import com.example.backend.models.MenuSection;
import com.example.backend.services.MenuSectionService;
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
@RequestMapping(path = "/api/menu-sections")
public class MenuSectionController {
    private final MenuSectionService menuSectionService;

    @Autowired
    public MenuSectionController(MenuSectionService menuSectionService) {
        this.menuSectionService = menuSectionService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<MenuSection>> getAllMenuSections() {
        List<MenuSection> menuSections = menuSectionService.getAllMenuSections();

        return ResponseEntity.status(HttpStatus.OK).body(menuSections);
    }
}

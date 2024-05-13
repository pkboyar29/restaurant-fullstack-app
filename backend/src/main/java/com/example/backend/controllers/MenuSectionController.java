package com.example.backend.controllers;

import com.example.backend.models.MenuSection;
import com.example.backend.services.MenuSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(path = "/api/menu-sections")
public class MenuSectionController {
    private final MenuSectionService menuSectionService;

    @Autowired
    public MenuSectionController(MenuSectionService menuSectionService) {
        this.menuSectionService = menuSectionService;
    }

    @GetMapping
    public ResponseEntity<List<MenuSection>> getAllMenuSections() {
        List<MenuSection> menuSections = menuSectionService.getAllMenuSections();

        return ResponseEntity.status(HttpStatus.OK).body(menuSections);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addMenuSection(@RequestBody MenuSection menuSectionDTO) {
        Map <String, String> responseBody = new HashMap<>();

        try {
            menuSectionService.addMenuSection(menuSectionDTO);
            responseBody.put("message", "Successful");
        }
        catch (Exception e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

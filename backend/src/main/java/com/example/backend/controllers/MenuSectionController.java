package com.example.backend.controllers;

import com.example.backend.exceptions.ObjectNotFoundException;
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

    @GetMapping(path = "/{id}")
    public ResponseEntity<MenuSection> getMenuSectionById(@PathVariable Long id) {
        try {
            MenuSection menuSection = menuSectionService.getMenuSectionById(id);
            return ResponseEntity.status(HttpStatus.OK).body(menuSection);
        }
        catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addMenuSection(@RequestBody MenuSection menuSectionDTO) {
        Map <String, String> responseBody = new HashMap<>();

        try {
            menuSectionService.addMenuSection(menuSectionDTO);
        }
        catch (Exception e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        responseBody.put("message", "Menu section successfully added");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deleteMenuSection(@PathVariable Long id) {
        Map <String, String> responseBody = new HashMap<>();

        try {
            menuSectionService.deleteMenuSection(id);
        }
        catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        responseBody.put("message", "Menu section successfully deleted");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> updateMenuSection(@PathVariable Long id, @RequestBody MenuSection menuSectionDTO) {
        Map <String, String> responseBody = new HashMap<>();

        try {
            menuSectionService.updateMenuSection(id, menuSectionDTO);
        }
        catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

        responseBody.put("message", "Menu section successfully updated");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

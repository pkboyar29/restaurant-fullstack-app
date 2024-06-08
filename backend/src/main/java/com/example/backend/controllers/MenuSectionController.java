package com.example.backend.controllers;

import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuSection;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.MenuSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/menu-sections")
public class MenuSectionController {
    private final MenuSectionService menuSectionService;
    private final UserRepository userRepository;

    @Autowired
    public MenuSectionController(MenuSectionService menuSectionService, UserRepository userRepository) {
        this.menuSectionService = menuSectionService;
        this.userRepository = userRepository;
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
    public ResponseEntity<Map<String, String>> addMenuSection(Authentication authentication, @RequestBody MenuSection menuSectionDTO) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Map <String, String> responseBody = new HashMap<>();
        if (userRepository.findByUsername(authentication.getName()).get().getRole().getName().equals("client")) {
            responseBody.put("message", "Permission denied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        try {
            menuSectionService.addMenuSection(menuSectionDTO);
        } catch (Exception e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        responseBody.put("message", "Menu section successfully added");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deleteMenuSection(Authentication authentication, @PathVariable Long id) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Map <String, String> responseBody = new HashMap<>();
        if (userRepository.findByUsername(authentication.getName()).get().getRole().getName().equals("client")) {
            responseBody.put("message", "Permission denied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        try {
            menuSectionService.deleteMenuSection(id);
        } catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        responseBody.put("message", "Menu section successfully deleted");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> updateMenuSection(Authentication authentication, @PathVariable Long id, @RequestBody MenuSection menuSectionDTO) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Map <String, String> responseBody = new HashMap<>();
        if (userRepository.findByUsername(authentication.getName()).get().getRole().getName().equals("client")) {
            responseBody.put("message", "Permission denied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        try {
            menuSectionService.updateMenuSection(id, menuSectionDTO);
        } catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        responseBody.put("message", "Menu section successfully updated");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

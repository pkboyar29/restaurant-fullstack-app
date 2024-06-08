package com.example.backend.controllers;

import com.example.backend.dto.MenuPosition.MenuPositionRequestDTO;
import com.example.backend.dto.MenuPosition.MenuPositionResponseDTO;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.services.MenuPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(path = "/api/menu-positions")
public class MenuPositionController {
    private final MenuPositionService menuPositionService;

    @Autowired
    public MenuPositionController(MenuPositionService menuPositionService) {
        this.menuPositionService = menuPositionService;
    }

    @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5172"})
    @GetMapping
    public ResponseEntity<List<MenuPositionResponseDTO>> getAllMenuPositions(@RequestParam(required = false) Long sectionId,
                                                                  @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable) {

        List<MenuPositionResponseDTO> menuPositions;
        if (sectionId != null) {
            try {
                if (onlyAvailable) {
                    menuPositions = menuPositionService.getAvailableMenuPositionsBySectionId(sectionId);
                } else {
                    menuPositions = menuPositionService.getMenuPositionsBySectionId(sectionId);
                }
            }
            catch (ObjectNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else {
            if (onlyAvailable) {
                menuPositions = menuPositionService.getAvailableMenuPositions();
            } else {
                menuPositions = menuPositionService.getAllMenuPositions();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(menuPositions);
    }

    @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5172"})
    @GetMapping(path = "/{id}")
    public ResponseEntity<MenuPositionResponseDTO> getMenuPositionById(@PathVariable Long id) {

        try {
            MenuPositionResponseDTO menuPositionResponseDTO = menuPositionService.getMenuPositionById(id);
            return ResponseEntity.status(HttpStatus.OK).body(menuPositionResponseDTO);
        }
        catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addMenuPosition(Authentication authentication, @ModelAttribute MenuPositionRequestDTO menuPositionRequestDTO) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map <String, String> responseBody = new HashMap<>();
        try {
            menuPositionService.addMenuPosition(menuPositionRequestDTO);
            responseBody.put("message", "Successful");
        }
        catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (Exception e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> updateMenuPosition(Authentication authentication, @ModelAttribute MenuPositionRequestDTO menuPositionRequestDTO, @PathVariable Long id) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map <String, String> responseBody = new HashMap<>();
        try {
            menuPositionService.updateMenuPosition(id, menuPositionRequestDTO);
        }
        catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        responseBody.put("message", "Successful");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deleteMenuPosition(Authentication authentication, @PathVariable Long id) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map <String, String> responseBody = new HashMap<>();
        try {
            menuPositionService.deleteMenuPosition(id);
        }
        catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
        responseBody.put("message", "Menu position successfully deleted");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

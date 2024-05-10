package com.example.backend.controllers;

import com.example.backend.dto.MenuPosition.MenuPositionRequestDTO;
import com.example.backend.dto.MenuPosition.MenuPositionResponseDTO;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuPosition;
import com.example.backend.services.MenuPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping
    public ResponseEntity<List<MenuPosition>> getAllMenuPositions(@RequestParam(required = false) Long sectionId) {

        List<MenuPosition> menuPositions;
        if (sectionId != null) {
            try {
                menuPositions = menuPositionService.getMenuPositionsBySectionId(sectionId);
            }
            catch (ObjectNotFoundException e) {
//                Map <String, String> responseBody = new HashMap<>();
//                body.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else {
            menuPositions = menuPositionService.getAllMenuPositions();
        }

        return ResponseEntity.status(HttpStatus.OK).body(menuPositions);
    }

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
    public ResponseEntity<Map<String, String>> addMenuPosition(@ModelAttribute MenuPositionRequestDTO menuPositionRequestDTO) {
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
    public ResponseEntity<Map<String, String>> updateMenuPosition(@ModelAttribute MenuPositionRequestDTO menuPositionRequestDTO, @PathVariable Long id) {
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
    public ResponseEntity<Map<String, String>> deleteMenuPosition(@PathVariable Long id) {
        Map <String, String> responseBody = new HashMap<>();
        try {
            menuPositionService.deleteMenuPosition(id);
        }
        catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (IOException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
        responseBody.put("message", "Menu position successfully deleted");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

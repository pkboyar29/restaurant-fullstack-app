package com.example.backend.controllers;

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

    @GetMapping(path = "/")
    public ResponseEntity<List<MenuPosition>> getAllMenuPositions(@RequestParam(required = false) Long sectionId) {

        List<MenuPosition> menuPositions;
        if (sectionId != null) {
            try {
                menuPositions = menuPositionService.getMenuPositionsBySectionId(sectionId);
            }
            catch (ObjectNotFoundException e) {
//                Map <String, String> body = new HashMap<>();
//                body.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else {
            menuPositions = menuPositionService.getAllMenuPositions();
        }

        return ResponseEntity.status(HttpStatus.OK).body(menuPositions);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, String>> deleteMenuPosition(@PathVariable Long id) {
        Map <String, String> body = new HashMap<>();
        try {
            menuPositionService.deleteMenuPosition(id);
        }
        catch (ObjectNotFoundException e) {
            body.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        catch (IOException e) {
            body.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        }
        body.put("message", "Menu position successfully deleted");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}

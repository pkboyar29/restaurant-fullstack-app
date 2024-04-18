package com.example.backend.controllers;

import com.example.backend.models.MenuPosition;
import com.example.backend.services.MenuPositionService;
import com.example.backend.services.MenuSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/menu-positions")
public class MenuPositionController {
    private final MenuPositionService menuPositionService;

    @Autowired
    public MenuPositionController(MenuPositionService menuPositionService) {
        this.menuPositionService = menuPositionService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<MenuPosition>> getAllMenuPositions() throws Exception {
        List<MenuPosition> allMenuPositions = menuPositionService.getAllMenuPositions();

        return ResponseEntity.status(HttpStatus.OK).body(allMenuPositions);
    }
}

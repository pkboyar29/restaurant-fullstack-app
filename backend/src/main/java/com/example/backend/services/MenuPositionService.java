package com.example.backend.services;

import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.models.MenuPosition;
import com.example.backend.repositories.MenuPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuPositionService {
    private final MenuPositionRepository menuPositionRepository;

    @Autowired
    public MenuPositionService(MenuPositionRepository menuPositionRepository) {
        this.menuPositionRepository = menuPositionRepository;
    }

    public List<MenuPosition> getAllMenuPositions() throws Exception {
        return menuPositionRepository.findAll();
    }

    public void deleteMenuPosition(Long id) throws ObjectNotFoundException {

        if (menuPositionRepository.existsById(id)) {
            menuPositionRepository.deleteById(id);
        }
        else {
            throw new ObjectNotFoundException("Menu position doesn't exist");
        }
    }
}

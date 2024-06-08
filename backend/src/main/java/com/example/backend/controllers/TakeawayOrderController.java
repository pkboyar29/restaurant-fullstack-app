package com.example.backend.controllers;

import com.example.backend.dto.TakeawayOrder.TakeawayOrderRequestDTO;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.exceptions.UserRoleException;
import com.example.backend.services.TakeawayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/takeaway-orders")
public class TakeawayOrderController {
    private final TakeawayOrderService takeawayOrderService;

    @Autowired
    public TakeawayOrderController(TakeawayOrderService takeawayOrderService) {
        this.takeawayOrderService = takeawayOrderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addTakeawayOrder(Authentication authentication, @RequestBody TakeawayOrderRequestDTO takeawayOrderRequestDTO) {
        String username = null;
        if (authentication == null) {
            System.out.println("authentication is null");
        } else {
            username = authentication.getName();
        }

        Map <String, String> responseBody = new HashMap<>();
        try {
            takeawayOrderService.addTakeawayOrder(username, takeawayOrderRequestDTO);

            responseBody.put("message", "Takeaway order add successfully");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (UserRoleException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        } catch (Exception e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}

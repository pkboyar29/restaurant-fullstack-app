package com.example.backend.controllers;

import com.example.backend.dto.Client.ClientResponseDTO;
import com.example.backend.dto.SignInRequestDTO;
import com.example.backend.dto.Client.ClientSignUpRequestDTO;
import com.example.backend.dto.Client.ClientUpdateContactRequestDTO;
import com.example.backend.exceptions.DuplicateClientException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.exceptions.UserException;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5172")
@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/client-sign-up")
    public ResponseEntity<Map<String, Object>> clientSignUp(@RequestBody ClientSignUpRequestDTO clientSignUpRequestDTO) {
        Map <String, Object> responseBody = new HashMap<>();

        try {
            responseBody = userService.clientSignUp(clientSignUpRequestDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
        catch (DuplicateClientException e) {
            responseBody.put("message", e.getMessage());
            responseBody.put("errorCode", e.getErrorCode());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @PostMapping(path = "/client-sign-in")
    public ResponseEntity<Map<String, Object>> clientSignIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        Map <String, Object> responseBody = new HashMap<>();

        try {
            responseBody = userService.clientSignIn(signInRequestDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (ObjectNotFoundException e ) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (UserException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @PostMapping(path = "/employee-sign-in")
    public ResponseEntity<Map<String, Object>> employeeSignIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        Map <String, Object> responseBody = new HashMap<>();

        try {
            responseBody = userService.employeeSignIn(signInRequestDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (ObjectNotFoundException e ) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
        catch (UserException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @PostMapping(path = "/update-client-contact")
    public ResponseEntity<Map<String, Object>> updateClientContact(Authentication authentication, @RequestBody ClientUpdateContactRequestDTO clientUpdateContactRequestDTO) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Map <String, Object> responseBody = new HashMap<>();
        try {
            ClientResponseDTO clientResponseDTO = userService.updateClientContact(authentication.getName(), clientUpdateContactRequestDTO);
            responseBody = userService.convertClientResponseDTOToResponseBody(clientResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @GetMapping(path = "/get-client-data")
    public ResponseEntity<Map<String, Object>> getClientData(Authentication authentication) {
        if (authentication == null) {
            System.out.println("authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        System.out.println(authentication);
        ClientResponseDTO clientResponseDTO = userService.getClientData(authentication.getName());

        Map <String, Object> responseBody = userService.convertClientResponseDTOToResponseBody(clientResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

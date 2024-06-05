package com.example.backend.controllers;

import com.example.backend.dto.Client.ClientResponseDTO;
import com.example.backend.dto.Client.ClientSignInRequestDTO;
import com.example.backend.dto.Client.ClientSignUpRequestDTO;
import com.example.backend.dto.Client.ClientUpdateContactRequestDTO;
import com.example.backend.exceptions.DuplicateClientException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.exceptions.UserException;
import com.example.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5172")
@RestController
@RequestMapping(path = "/api/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(path = "/sign-up")
    public ResponseEntity<Map<String, Object>> clientSignUp(@RequestBody ClientSignUpRequestDTO clientSignUpRequestDTO) {
        Map <String, Object> responseBody = new HashMap<>();

        try {
            ClientResponseDTO clientResponseDTO = clientService.signUp(clientSignUpRequestDTO);
            responseBody = convertClientResponseDTOToResponseBody(clientResponseDTO);

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

    @PostMapping(path = "/sign-in")
    public ResponseEntity<Map<String, Object>> clientSignIn(@RequestBody ClientSignInRequestDTO clientSignInRequestDTO) {
        Map <String, Object> responseBody = new HashMap<>();

        try {
            ClientResponseDTO clientResponseDTO = clientService.signIn(clientSignInRequestDTO);
            responseBody = convertClientResponseDTOToResponseBody(clientResponseDTO);

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

    @PatchMapping(path = "/update-contact")
    public ResponseEntity<Map<String, Object>> updateClientContact(@RequestBody ClientUpdateContactRequestDTO clientUpdateContactRequestDTO) {
        Map <String, Object> responseBody = new HashMap<>();

        try {
            ClientResponseDTO clientResponseDTO = clientService.updateClientContact(clientUpdateContactRequestDTO);
            responseBody = convertClientResponseDTOToResponseBody(clientResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (ObjectNotFoundException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    private Map<String, Object> convertClientResponseDTOToResponseBody(ClientResponseDTO clientResponseDTO) {
        Map <String, Object> responseBody = new HashMap<>();
        responseBody.put("id", clientResponseDTO.getId().toString());
        responseBody.put("firstName", clientResponseDTO.getFirstName());
        responseBody.put("lastName", clientResponseDTO.getLastName());
        responseBody.put("patronymic", clientResponseDTO.getPatronymic());
        responseBody.put("username", clientResponseDTO.getUsername());
        responseBody.put("phone", clientResponseDTO.getPhone());
        responseBody.put("email", clientResponseDTO.getEmail());
        responseBody.put("orderDiscount", clientResponseDTO.getOrderDiscount());
        responseBody.put("numberOrders", clientResponseDTO.getNumberOrders());

        return responseBody;
    }
}

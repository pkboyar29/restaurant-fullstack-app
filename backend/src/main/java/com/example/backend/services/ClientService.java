package com.example.backend.services;

import com.example.backend.dto.Client.ClientSignInRequestDTO;
import com.example.backend.dto.Client.ClientSignUpRequestDTO;
import com.example.backend.exceptions.DuplicateClientException;
import com.example.backend.models.Client;
import com.example.backend.repositories.ClientRepository;
import com.example.backend.repositories.OrderDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final OrderDiscountRepository orderDiscountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, OrderDiscountRepository orderDiscountRepository) {
        this.clientRepository = clientRepository;
        this.orderDiscountRepository = orderDiscountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void signUp(ClientSignUpRequestDTO clientSignUpRequestDTO) {

        if (clientRepository.existsByUsername(clientSignUpRequestDTO.getUsername())) {
            throw new DuplicateClientException("DUPLICATE_USERNAME", "Client with this username already exists");
        }

        if (clientRepository.existsByPhone(clientSignUpRequestDTO.getPhone())) {
            throw new DuplicateClientException("DUPLICATE_PHONE", "Client with this phone already exists");
        }

        if (clientRepository.existsByEmail(clientSignUpRequestDTO.getEmail())) {
            throw new DuplicateClientException("DUPLICATE_EMAIL", "Client with this email already exists");
        }

        Client newClient = new Client();

        newClient.setUsername(clientSignUpRequestDTO.getUsername());
        newClient.setFirstName(clientSignUpRequestDTO.getFirstName());
        newClient.setLastName(clientSignUpRequestDTO.getLastName());
        newClient.setPatronymic(clientSignUpRequestDTO.getPatronymic());
        String encodedPassword = passwordEncoder.encode(clientSignUpRequestDTO.getPassword());
        newClient.setPassword(encodedPassword);
        newClient.setPhone(clientSignUpRequestDTO.getPhone());
        newClient.setEmail(clientSignUpRequestDTO.getEmail());
        newClient.setGender(clientSignUpRequestDTO.getGender());
        newClient.setNumberOrders(0);
        newClient.setOrderDiscount(orderDiscountRepository.findById((long) 1).get());
        newClient.setDateLastLogin(LocalDateTime.now());

        try {
            clientRepository.save(newClient);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // возвращать id пользователю (на будущее?)
    }

    public void signIn(ClientSignInRequestDTO clientSignInRequestDTO) {
    }
}

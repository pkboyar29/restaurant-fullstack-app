package com.example.backend.services;

import com.example.backend.dto.Client.ClientResponseDTO;
import com.example.backend.dto.Client.ClientSignInRequestDTO;
import com.example.backend.dto.Client.ClientSignUpRequestDTO;
import com.example.backend.exceptions.DuplicateClientException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.exceptions.UserException;
import com.example.backend.models.Client;
import com.example.backend.repositories.ClientRepository;
import com.example.backend.repositories.OrderDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public ClientResponseDTO signUp(ClientSignUpRequestDTO clientSignUpRequestDTO) {

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
            Client client = clientRepository.save(newClient);

            ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
            clientResponseDTO.setId(client.getId());
            clientResponseDTO.setFirstName(client.getFirstName());
            clientResponseDTO.setPhone(client.getPhone());
            clientResponseDTO.setUsername(client.getUsername());
            clientResponseDTO.setOrderDiscount(client.getOrderDiscount());

            return clientResponseDTO;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ClientResponseDTO signIn(ClientSignInRequestDTO clientSignInRequestDTO) {
        Optional<Client> optionalClient = clientRepository.findByUsername(clientSignInRequestDTO.getUsername());

        if (optionalClient.isEmpty()) {
            throw new ObjectNotFoundException("Client with this username doesn't exist");
        }

        Client client = optionalClient.get();
        String passwordFromDB = client.getPassword();
        String passwordFromRequest = clientSignInRequestDTO.getPassword();

        if (!passwordEncoder.matches(passwordFromRequest, passwordFromDB)) {
            throw new UserException("Password doesn't match");
        }

        client.setDateLastLogin(LocalDateTime.now());
        clientRepository.save(client);

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId(client.getId());
        clientResponseDTO.setFirstName(client.getFirstName());
        clientResponseDTO.setPhone(client.getPhone());
        clientResponseDTO.setUsername(client.getUsername());
        clientResponseDTO.setOrderDiscount(client.getOrderDiscount());
        System.out.println();

        return clientResponseDTO;
    }
}

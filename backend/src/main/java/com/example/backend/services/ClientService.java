package com.example.backend.services;

import com.example.backend.dto.Client.ClientResponseDTO;
import com.example.backend.dto.Client.ClientSignInRequestDTO;
import com.example.backend.dto.Client.ClientSignUpRequestDTO;
import com.example.backend.dto.Client.ClientUpdateContactRequestDTO;
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

            return convertClientToClientResponseDTO(client);
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

        return convertClientToClientResponseDTO(client);
    }

    public ClientResponseDTO updateClientContact(ClientUpdateContactRequestDTO clientUpdateContactRequestDTO) {
        System.out.println("id из dto = " + clientUpdateContactRequestDTO.getId());
        Optional<Client> optionalClient = clientRepository.findById(clientUpdateContactRequestDTO.getId());
        if (optionalClient.isEmpty()) {
            throw new ObjectNotFoundException("Client with this id doesn't exist");
        }
        Client client = optionalClient.get();

        try {
            client.setFirstName(clientUpdateContactRequestDTO.getFirstName());
            client.setLastName(clientUpdateContactRequestDTO.getLastName());
            client.setPatronymic(clientUpdateContactRequestDTO.getPatronymic());
            client.setPhone(clientUpdateContactRequestDTO.getPhone());
            client.setEmail(clientUpdateContactRequestDTO.getEmail());
            clientRepository.save(client);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return convertClientToClientResponseDTO(client);
    }

    private ClientResponseDTO convertClientToClientResponseDTO(Client client) {
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId(client.getId());
        clientResponseDTO.setFirstName(client.getFirstName());
        clientResponseDTO.setLastName(client.getLastName());
        clientResponseDTO.setPatronymic(client.getPatronymic());
        clientResponseDTO.setPhone(client.getPhone());
        clientResponseDTO.setEmail(client.getEmail());
        clientResponseDTO.setUsername(client.getUsername());
        clientResponseDTO.setOrderDiscount(client.getOrderDiscount());
        clientResponseDTO.setNumberOrders(client.getNumberOrders());

        return clientResponseDTO;
    }
}

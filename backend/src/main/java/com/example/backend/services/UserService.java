package com.example.backend.services;

import com.example.backend.dto.Client.ClientResponseDTO;
import com.example.backend.dto.SignInRequestDTO;
import com.example.backend.dto.Client.ClientSignUpRequestDTO;
import com.example.backend.dto.Client.ClientUpdateContactRequestDTO;
import com.example.backend.exceptions.DuplicateClientException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.exceptions.UserException;
import com.example.backend.models.Client;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.repositories.ClientRepository;
import com.example.backend.repositories.OrderDiscountRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.JWTCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderDiscountRepository orderDiscountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTCore jwtCore;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(ClientRepository clientRepository, UserRepository userRepository,
                       RoleRepository roleRepository, OrderDiscountRepository orderDiscountRepository, JWTCore jwtCore, AuthenticationManager authenticationManager) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderDiscountRepository = orderDiscountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtCore = jwtCore;
        this.authenticationManager = authenticationManager;
    }

    public Map<String, Object> signUp(ClientSignUpRequestDTO clientSignUpRequestDTO) {
        if (userRepository.existsByUsername(clientSignUpRequestDTO.getUsername())) {
            throw new DuplicateClientException("DUPLICATE_USERNAME", "Client with this username already exists");
        }
        if (userRepository.existsByPhone(clientSignUpRequestDTO.getPhone())) {
            throw new DuplicateClientException("DUPLICATE_PHONE", "Client with this phone already exists");
        }
        if (userRepository.existsByEmail(clientSignUpRequestDTO.getEmail())) {
            throw new DuplicateClientException("DUPLICATE_EMAIL", "Client with this email already exists");
        }

        User newUser = new User();
        Client newClient = new Client();

        newUser.setUsername(clientSignUpRequestDTO.getUsername());
        newUser.setFirstName(clientSignUpRequestDTO.getFirstName());
        newUser.setLastName(clientSignUpRequestDTO.getLastName());
        newUser.setPatronymic(clientSignUpRequestDTO.getPatronymic());
        String encodedPassword = passwordEncoder.encode(clientSignUpRequestDTO.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setPhone(clientSignUpRequestDTO.getPhone());
        newUser.setEmail(clientSignUpRequestDTO.getEmail());
        newUser.setGender(clientSignUpRequestDTO.getGender());
        newUser.setDateLastLogin(LocalDateTime.now());

        Optional<Role> optionalRole = roleRepository.findById(1L);
        newUser.setRole(optionalRole.get());

        newClient.setUser(newUser);
        newClient.setNumberOrders(0);
        newClient.setOrderDiscount(orderDiscountRepository.findById((long) 1).get());

        try {
            userRepository.save(newUser);
            clientRepository.save(newClient);

            SignInRequestDTO signInRequestDTO = new SignInRequestDTO();
            signInRequestDTO.setUsername(clientSignUpRequestDTO.getUsername());
            signInRequestDTO.setPassword(clientSignUpRequestDTO.getPassword());

            return signIn(signInRequestDTO);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map<String, Object> signIn(SignInRequestDTO signInRequestDTO) {
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequestDTO.getUsername(), signInRequestDTO.getPassword())
            );
        } catch (BadCredentialsException e) {
            System.out.println("bad credentials exception error" + e);
            throw new UserException("sad");
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtCore.generateToken(auth);
        System.out.println("generated token is " + jwt);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", jwt);
        System.out.println("responseBody " + responseBody);
        return responseBody;
    }

    public ClientResponseDTO updateClientContact(ClientUpdateContactRequestDTO clientUpdateContactRequestDTO) {
        Optional<User> optionalUser = userRepository.findById(clientUpdateContactRequestDTO.getId());
        if (optionalUser.isEmpty()) {
            throw new ObjectNotFoundException("User with this id doesn't exist");
        }
        User user = optionalUser.get();

        try {
            user.setFirstName(clientUpdateContactRequestDTO.getFirstName());
            user.setLastName(clientUpdateContactRequestDTO.getLastName());
            user.setPatronymic(clientUpdateContactRequestDTO.getPatronymic());
            user.setPhone(clientUpdateContactRequestDTO.getPhone());
            user.setEmail(clientUpdateContactRequestDTO.getEmail());
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        Optional<Client> optionalClient = clientRepository.findByUser(user);
        if (optionalClient.isEmpty()) {
            throw new ObjectNotFoundException("Client with this user_id doesn't exist");
        }

        return createClientResponseDTO(user, optionalClient.get());
    }

    private ClientResponseDTO createClientResponseDTO(User user, Client client) {
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId(user.getId());
        clientResponseDTO.setFirstName(user.getFirstName());
        clientResponseDTO.setLastName(user.getLastName());
        clientResponseDTO.setPatronymic(user.getPatronymic());
        clientResponseDTO.setPhone(user.getPhone());
        clientResponseDTO.setEmail(user.getEmail());
        clientResponseDTO.setUsername(user.getUsername());
        clientResponseDTO.setOrderDiscount(client.getOrderDiscount());
        clientResponseDTO.setNumberOrders(client.getNumberOrders());

        return clientResponseDTO;
    }
}

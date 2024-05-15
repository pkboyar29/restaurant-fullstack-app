package com.example.backend.repositories;

import com.example.backend.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByPhone(String phone);
    Boolean existsByEmail(String email);
}

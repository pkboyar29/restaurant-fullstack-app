package com.example.backend.repositories;

import com.example.backend.models.OrderDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {
}

package com.example.backend.repositories;

import com.example.backend.models.TakeawayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeawayOrderRepository extends JpaRepository<TakeawayOrder, Long> {
}

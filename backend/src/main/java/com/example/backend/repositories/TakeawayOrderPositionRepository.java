package com.example.backend.repositories;

import com.example.backend.models.TakeawayOrder;
import com.example.backend.models.TakeawayOrderPosition;
import com.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeawayOrderPositionRepository extends JpaRepository<TakeawayOrderPosition, Long> {
    List<TakeawayOrderPosition> findByTakeawayOrder(TakeawayOrder takeawayOrder);
}

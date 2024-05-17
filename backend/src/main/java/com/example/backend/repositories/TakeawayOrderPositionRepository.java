package com.example.backend.repositories;

import com.example.backend.models.TakeawayOrderPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeawayOrderPositionRepository extends JpaRepository<TakeawayOrderPosition, Long> {
}

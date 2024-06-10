package com.example.backend.repositories;

import com.example.backend.models.TakeawayOrder;
import com.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeawayOrderRepository extends JpaRepository<TakeawayOrder, Long> {
    List<TakeawayOrder> findByUser(User user);
}

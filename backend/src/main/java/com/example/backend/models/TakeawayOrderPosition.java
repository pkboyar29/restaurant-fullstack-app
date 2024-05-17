package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "takeaway_order_positions")
public class TakeawayOrderPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_position", nullable = false)
    MenuPosition menuPosition;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "takeaway_order", nullable = false)
    TakeawayOrder takeawayOrder;
}

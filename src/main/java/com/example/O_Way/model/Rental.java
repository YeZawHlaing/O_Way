package com.example.O_Way.model;

import com.example.O_Way.util.status.Rental_Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long distance;

    @Column
    private LocalDateTime rental_time;

    @Column
    private double estimate_cost;

    @Column
    private LocalDateTime paid_at;

    @Column
    private Double pickupLatitude;

    @Column
    private Double pickupLongitude;

    @Column
    private Double dropLatitude;

    @Column
    private Double dropLongitude;

    @Enumerated(EnumType.STRING)
    private Rental_Status rentalStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;




}

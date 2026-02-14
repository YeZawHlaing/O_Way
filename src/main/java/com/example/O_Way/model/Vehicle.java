package com.example.O_Way.model;

import com.example.O_Way.util.status.Driver_Status;
import com.example.O_Way.util.status.Vehicle_Status;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String PlateNumber;

    @Column(unique = true, nullable = false)
    private String contact;

    @Column(unique = true,nullable = false)
    private String nrc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Vehicle_Status vehicleStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Driver_Status driverStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Rental> rentals = new ArrayList<>();

}

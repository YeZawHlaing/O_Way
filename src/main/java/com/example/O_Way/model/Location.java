package com.example.O_Way.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    private Rental rental;
}

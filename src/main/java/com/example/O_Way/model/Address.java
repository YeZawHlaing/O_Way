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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String city;

    @Column
    private String township;

    @Column
    private String road;

    @Column
    private String street;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Vehicle vehicle;
}

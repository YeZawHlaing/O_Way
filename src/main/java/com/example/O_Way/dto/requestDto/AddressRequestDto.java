package com.example.O_Way.dto.requestDto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddressRequestDto {

    private String city;
    private String township;
    private String road;
    private String street;
}

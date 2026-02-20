package com.example.O_Way.config;

import com.example.O_Way.dto.responseDto.ProfileResponseDto;
import com.example.O_Way.model.Profile;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configure custom mappings for Profile → ProfileResponseDto
        modelMapper.typeMap(Profile.class, ProfileResponseDto.class)
                .addMapping(Profile::getFullName, ProfileResponseDto::setFullName)
                .addMapping(Profile::getContact, ProfileResponseDto::setContact);
//                .addMapping(Profile::getLocation, ProfileResponseDto::setLatitude);
        // Uncomment and map the rest if needed
        //.addMapping(Profile::getLon, ProfileResponseDto::setLongitude)
        //.addMapping(Profile::getProfilePic, ProfileResponseDto::setProfilePic)
        //.addMapping(Profile::getDateOfBirth, ProfileResponseDto::setDob);

        return modelMapper;
    }
}
package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.ProfileRequestDto;
import com.example.O_Way.dto.responseDto.VehicleResponseDto;
import com.example.O_Way.service.ProfileService;
import com.example.O_Way.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final VehicleService vehicleService;

//    @PostMapping("/profile")
//    public ResponseEntity<ApiResponse> createProfile(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @Valid @RequestBody ProfileRequestDto request
//    ) {
//
//        String username = userDetails.getUsername();
//
//        ApiResponse response = profileService.createProfile(username, request);
//
//        return ResponseEntity.ok(response);
//    }


    // -------------------------------
    // Update Profile (without file)
    // -------------------------------
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileRequestDto profileRequest
    ) {
        ApiResponse response = profileService.updateProfile(userId, profileRequest);
        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Get Profile by User ID
    // -------------------------------
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable Long userId) {
        ApiResponse response = profileService.getProfileById(userId);
        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Upload Profile Picture
    // -------------------------------
    @PostMapping("/{userId}/upload")
    public ResponseEntity<ApiResponse> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        String fileUrl = profileService.uploadProfilePicture(userId, file);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(1)
                        .code(200)
                        .message("Profile picture uploaded successfully")
                        .data(fileUrl)
                        .build()
        );
    }

    @GetMapping("/getAllprofile")
    public ResponseEntity<ApiResponse> getProfile(Authentication authentication) {

        String username = authentication.getName(); // from JWT

        ApiResponse response = profileService.getProfileByUsername(username);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/myProfile")
    public ResponseEntity<ApiResponse> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    // UPDATE PROFILE
    @PatchMapping
    public ResponseEntity<ApiResponse> updateProfile(
            @Valid @RequestBody ProfileRequestDto request
    ) {
        return ResponseEntity.ok(profileService.updateProfileByUser(request));
    }

//    @GetMapping("/getVehicle")
//    public ResponseEntity<List<VehicleResponseDto>> getVehicles() {
//        return ResponseEntity.ok(vehicleService.getAllVehicles());
//    }
}

package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.ProfileRequestDto;
import com.example.O_Way.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createProfile(
            @RequestParam("userId") Long userId,
            @RequestBody ProfileRequestDto profileRequest
    ) {
        // Directly call service, no @Valid needed
        ApiResponse response = profileService.createProfile(userId, profileRequest);
        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Create Profile (with optional file)
    // -------------------------------
//    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ResponseEntity<ApiResponse> createProfile(
//            @RequestParam("userId") Long userId,
//            @Valid @RequestPart("profile") ProfileRequestDto profileRequest,
//            @RequestPart(value = "file", required = false) MultipartFile file
//    ) {
//        ApiResponse response = profileService.createProfile(userId, profileRequest, file);
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
}

package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.common.storage.StorageService;
import com.example.O_Way.common.storage.StorageServiceFactory;
import com.example.O_Way.dto.requestDto.ProfileRequestDto;
import com.example.O_Way.dto.responseDto.ProfileResponseDto;
import com.example.O_Way.model.Location;
import com.example.O_Way.model.Profile;
import com.example.O_Way.model.User;
import com.example.O_Way.repo.ProfileRepo;
import com.example.O_Way.repo.RolesRepo;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImp implements ProfileService {



    private final UserRepo userRepository;
    private final ProfileRepo profileRepository;
    private final RolesRepo roleRepository;
    private final ModelMapper modelMapper;
    private StorageService storageService;

    @Autowired
    public void setStorageService(StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
    }

    @jakarta.transaction.Transactional
    public String uploadProfilePicture(final Long userId, final MultipartFile file) {
        final Profile profile = this.profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found for user ID: " + userId));


        if (file != null && !file.isEmpty()) {
            String filename = storageService.store(file);
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(filename)
                    .toUriString();
            profile.setProfilePic(fileUrl);
            this.profileRepository.save(profile);
            return fileUrl;

        } else {
            return profile.getProfilePic();
        }

    }

    @Override
    public ApiResponse createProfile(Long userId, ProfileRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if profile already exists
        if (profileRepository.findByUser_Id(userId).isPresent()) {
            throw new RuntimeException("Profile already exists for this user");
        }

        Profile profile = new Profile();

        // 🔥 SET FIELDS MANUALLY
        profile.setFullName(request.getFullName());
        profile.setEmail(request.getEmail());
        profile.setContact(request.getContact());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
        profile.setProfilePic(null);
        profile.setUser(user);

        // Create Location
        Location location = new Location();
        location.setLatitude(request.getLocationRequestDto().getLatitude());
        location.setLongitude(request.getLocationRequestDto().getLongitude());

        profile.setLocation(location);

        profileRepository.save(profile);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Profile created successfully")
                .data(profile)
                .build();
    }

    @jakarta.transaction.Transactional
    @Override
    public ApiResponse updateProfile(final Long userId, final ProfileRequestDto profileRequest) {
        Profile profile = this.profileRepository.findByUser_Id(userId).orElseThrow(() -> new EntityNotFoundException("Profile not found for user ID: " + userId));

        modelMapper.map(profileRequest,profile);


        this.profileRepository.save(profile);

        ProfileResponseDto response = modelMapper.map(profile, ProfileResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .meta(null)
                .data(response)
                .message("Profile updated successfully")
                .build();
    }

    @Override
    public ApiResponse getProfileById(final Long userId) {
        Profile profile = this.profileRepository.findByUser_Id(userId).orElseThrow(() -> new EntityNotFoundException("Profile not found for user ID: " + userId));

        ProfileResponseDto response = modelMapper.map(profile, ProfileResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .data(response)
                .message("Profile fetched successfully")
                .build();
    }
}

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ApiResponse getMyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByName(username);


        Profile profile = profileRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        ProfileResponseDto response =
                modelMapper.map(profile, ProfileResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Profile fetched successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse createProfile(final String username, final ProfileRequestDto request){
// 1️⃣ Find logged-in user by username
        User user = userRepository.findByName(username);

        // 2️⃣ Prevent duplicate profile
        if (user.getProfile() != null) {
            throw new IllegalStateException("Profile already exists for this user");
        }

        // 3️⃣ Create Profile
        Profile profile = new Profile();
        profile.setFullName(request.getFullName());
        profile.setEmail(request.getEmail());
        profile.setContact(request.getContact());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
        profile.setProfilePic(null);

        // 4️⃣ Map Location
        Location location = new Location();
        location.setLatitude(request.getLocationRequestDto().getLatitude());
        location.setLongitude(request.getLocationRequestDto().getLongitude());

        profile.setLocation(location);

        // 5️⃣ Set relationship
        profile.setUser(user);
        user.setProfile(profile);

        profileRepository.save(profile);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Profile created successfully")
                .data(null)
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

    @Override
    public ApiResponse getProfileByUsername(String username) {
        User user = userRepository.findByName(username);


        Profile profile = profileRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        ProfileResponseDto dto = modelMapper.map(profile, ProfileResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .data(dto)
                .message("Profile fetched successfully")
                .build();
    }

    @Override
    public ApiResponse updateProfileByUser(ProfileRequestDto request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByName(username);

        Profile profile = profileRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        // Update fields
        profile.setFullName(request.getFullName());
        profile.setEmail(request.getEmail());
        profile.setContact(request.getContact());
        profile.setDob(request.getDob());
        profile.setGender(request.getGender());
//        profile.setProfilePic(request.getProfilePic());

        profileRepository.save(profile);

        ProfileResponseDto response =
                modelMapper.map(profile, ProfileResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Profile updated successfully")
                .data(response)
                .build();
    }
}

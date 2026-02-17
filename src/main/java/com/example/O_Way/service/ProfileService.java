package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.ProfileRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    public String uploadProfilePicture(final Long userId, final MultipartFile file);

    public ApiResponse createProfile(final String username, final ProfileRequestDto profileRequest);
//    public ApiResponse softDeleteProfile(final Long userId);

    public ApiResponse updateProfile(final Long userId,final ProfileRequestDto profileRequest);

    public ApiResponse getProfileById(final Long userId);

    public ApiResponse getProfileByUsername(String username);

    ApiResponse updateProfileByUser(ProfileRequestDto request);

    public ApiResponse getMyProfile();


}

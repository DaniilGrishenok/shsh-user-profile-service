package com.shsh.user_profile_service.service;

import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    public UserProfile findById(UUID id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile update(UUID id, UserProfile userProfile) {
        UserProfile existingProfile = findById(id);
        existingProfile.setUsername(userProfile.getUsername());
        existingProfile.setEmail(userProfile.getEmail());
        existingProfile.setDescriptionOfProfile(userProfile.getDescriptionOfProfile());
        existingProfile.setStatus(userProfile.getStatus());
        return userProfileRepository.save(existingProfile);
    }

    public void delete(UUID id) {
        userProfileRepository.deleteById(id);
    }
}
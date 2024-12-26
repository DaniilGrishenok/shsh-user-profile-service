package com.shsh.user_profile_service.service;

import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    @Transactional
    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }
    @Transactional
    public UserProfile getProfileById(String id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }
    @Transactional
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public UserProfile update(String id, @Valid UserProfile userProfile) {
        UserProfile existingProfile = getProfileById(id);
        if (!existingProfile.getUsername().equals(userProfile.getUsername()))
            existingProfile.setUsername(userProfile.getUsername());
        if (!existingProfile.getEmail().equals(userProfile.getEmail()))
            existingProfile.setEmail(userProfile.getEmail());
        if (!existingProfile.getDescriptionOfProfile().equals(userProfile.getDescriptionOfProfile()))
            existingProfile.setDescriptionOfProfile(userProfile.getDescriptionOfProfile());
        if (!existingProfile.getStatus().equals(userProfile.getStatus()))
            existingProfile.setStatus(userProfile.getStatus());
        return userProfileRepository.save(existingProfile);
    }
    @Transactional
    public void delete(String id) {
        userProfileRepository.deleteById(id);
    }

    public UserProfile createUserProfile(CreateUserProfileRequest request) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(request.getId());
        userProfile.setUsername(request.getUsername()); // Проверьте, что здесь используется username
        userProfile.setEmail(request.getEmail());
        userProfile.setDescriptionOfProfile("");
        userProfile.setStatus("");
        return userProfileRepository.save(userProfile);
    }

    public List<UserProfile> findUsersByUsernameSubstring(String substring) {
        return userProfileRepository.findByUsernameContainingIgnoreCase(substring);
    }

    // Получение ID пользователя по username
    public String getIdByUsername(String username) {
        Optional<UserProfile> user = userProfileRepository.findByUsername(username);
        return user.map(UserProfile::getId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с username '" + username + "' не найден"));
    }

    public boolean existsById(String userId) {
        return userProfileRepository.existsById(userId);
    }
}
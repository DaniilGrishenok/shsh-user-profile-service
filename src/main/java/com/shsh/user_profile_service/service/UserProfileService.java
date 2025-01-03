package com.shsh.user_profile_service.service;

import com.shsh.user_profile_service.dto.CreateUserProfileRequest;
import com.shsh.user_profile_service.dto.UpdateUserProfileRequest;
import com.shsh.user_profile_service.dto.UserStatus;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final S3Service s3Service;
    private final UserProfileRepository userProfileRepository;
    private final RedisTemplate<String, String> redisTemplate;
    public List<UserStatus> getStatuses(List<String> userIds) {
        return userIds.stream()
                .map(this::getStatus)
                .toList();
    }
    public String uploadAvatarAndUpdateProfile(String userId, MultipartFile file) throws IOException {
        // Загрузка файла в S3
        String avatarUrl = s3Service.uploadAvatar(userId, file);

        // Обновление ссылки на аватар в базе данных
        Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserProfile user = userOptional.get();
            user.setAvatarUrl(avatarUrl);
            userProfileRepository.save(user);
            return "Аватар успешно загружен и сохранен!";
        } else {
            throw new RuntimeException("Profile not found");
        }
    }
    public boolean deleteAvatarAndUpdateProfile(String userId) {

        Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserProfile user = userOptional.get();
            String avatarUrl = user.getAvatarUrl();

            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                String fileName = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
                s3Service.deleteAvatarFromS3(userId, fileName);

                user.setAvatarUrl(null);
                userProfileRepository.save(user);

                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public UserStatus getStatus(String userId) {
        String status = redisTemplate.opsForValue().get("user:" + userId);
        if (status == null) {
            status = "offline";
        }

        String lastPingAt = redisTemplate.opsForValue().get("user:" + userId + ":lastPingAt");
        LocalDateTime lastSeen = null;
        if (lastPingAt != null) {
            lastSeen = Instant.ofEpochMilli(Long.parseLong(lastPingAt)).atZone(ZoneId.of("UTC")).toLocalDateTime();
        }

        return new UserStatus(userId, status, lastSeen != null ? lastSeen.toString() : null);
    }
    @Transactional
    public boolean checkAndUpdatePremiumStatus(String userId) {
        UserProfile profile = getProfileById(userId);

        if (profile.isPremium() && (profile.getPremiumExpiresAt() == null || profile.getPremiumExpiresAt().isBefore(LocalDateTime.now()))) {
            profile.setPremium(false);
            userProfileRepository.save(profile);
            return false;
        }
        return profile.isPremium();
    }
    /*
    * метод установки премиума
    * метод бана пользователей
    * метод проверки забанен ли пользак
    * метод
    * */

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
    public UserProfile update(@Valid UpdateUserProfileRequest request) {
        UserProfile existingProfile = userProfileRepository.findByUsername(request.getUsername());

        if (request.getUsername() != null && !Objects.equals(existingProfile.getUsername(), request.getUsername())) {
            existingProfile.setUsername(request.getUsername());
        }
        if (request.getEmail() != null && !Objects.equals(existingProfile.getEmail(), request.getEmail())) {
            existingProfile.setEmail(request.getEmail());
        }
        if (request.getDescriptionOfProfile() != null && !Objects.equals(existingProfile.getDescriptionOfProfile(), request.getDescriptionOfProfile())) {
            existingProfile.setDescriptionOfProfile(request.getDescriptionOfProfile());
        }
        if (request.getStatus() != null && !Objects.equals(existingProfile.getStatus(), request.getStatus())) {
            existingProfile.setStatus(request.getStatus());
        }
        if (request.getAvatarUrl() != null && !Objects.equals(existingProfile.getAvatarUrl(), request.getAvatarUrl())) {
            existingProfile.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getChatWallpaperUrl() != null && !Objects.equals(existingProfile.getChatWallpaperUrl(), request.getChatWallpaperUrl())) {
            existingProfile.setChatWallpaperUrl(request.getChatWallpaperUrl());
        }
        if (request.getGender() != null && !Objects.equals(existingProfile.getGender(), request.getGender())) {
            existingProfile.setGender(request.getGender());
        }
        if (request.getDateOfBirth() != null && !Objects.equals(existingProfile.getDateOfBirth(), request.getDateOfBirth())) {
            existingProfile.setDateOfBirth(request.getDateOfBirth());
        }

        existingProfile.setLastUpdated(LocalDateTime.now());
        return userProfileRepository.save(existingProfile);
    }


    @Transactional
    public boolean updatePremiumStatus(String userId, Boolean update){
        UserProfile userProfile = getProfileById(userId);
        userProfile.setPremium(update);
        return true;
    }

    @Transactional
    public void delete(String id) {
        userProfileRepository.deleteById(id);
    }

    public UserProfile createUserProfile(CreateUserProfileRequest request) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(request.getId());
        userProfile.setUsername(request.getUsername());
        userProfile.setEmail(request.getEmail());
        userProfile.setDescriptionOfProfile("");
        userProfile.setStatus("");
        userProfile.setGender(null);
        return userProfileRepository.save(userProfile);
    }

    public List<UserProfile> findUsersByUsernameSubstring(String substring) {
        return userProfileRepository.findByUsernameContainingIgnoreCase(substring);
    }

    public String getIdByUsername(String username) {
        Optional<UserProfile> user = Optional.ofNullable(userProfileRepository.findByUsername(username));
        return user.map(UserProfile::getId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с username '" + username + "' не найден"));
    }

    public boolean existsById(String userId) {
        return userProfileRepository.existsById(userId);
    }
}
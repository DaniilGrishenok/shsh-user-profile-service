package com.shsh.user_profile_service.service;

import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final S3Service s3Service;
    private final UserProfileRepository userProfileRepository;
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
    // Метод для загрузки и обновления обоев пользователя
    public String uploadGlobalWallpaperAndUpdateProfile(String userId, MultipartFile file) throws IOException {
        // Проверим, если у пользователя уже есть обои, то удалим их из S3
        Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserProfile user = userOptional.get();
            String existingWallpaperUrl = user.getChatWallpaperUrl();

            if (existingWallpaperUrl != null && !existingWallpaperUrl.isEmpty()) {
                String oldFileName = existingWallpaperUrl.substring(existingWallpaperUrl.lastIndexOf("/") + 1);
                s3Service.deleteGlobalWallpaperFromS3(userId, oldFileName);
            }

            String newWallpaperUrl = s3Service.uploadGlobalWallpaper(userId, file);

            user.setChatWallpaperUrl(newWallpaperUrl);
            userProfileRepository.save(user);

            return "Обои успешно загружены и сохранены!";
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    // Удаление глобальных обоев и обновление профиля
    public boolean deleteGlobalWallpaperAndUpdateProfile(String userId) {

        Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserProfile user = userOptional.get();
            String wallpaperUrl = user.getChatWallpaperUrl();

            if (wallpaperUrl != null && !wallpaperUrl.isEmpty()) {
                String fileName = wallpaperUrl.substring(wallpaperUrl.lastIndexOf("/") + 1);
                s3Service.deleteGlobalWallpaperFromS3(userId, fileName);

                user.setChatWallpaperUrl(null);
                userProfileRepository.save(user);

                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

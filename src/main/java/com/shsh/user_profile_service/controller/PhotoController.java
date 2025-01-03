package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.service.S3Service;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final S3Service s3Service;
    private final UserProfileService userProfileService;

    @PostMapping("/upload-avatar/{userId}")
    public String uploadAvatar(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        try {
            return userProfileService.uploadAvatarAndUpdateProfile(userId, file);
        } catch (IOException e) {
            return "Ошибка загрузки аватара: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Ошибка: " + e.getMessage();
        }
    }
    @DeleteMapping("/delete-avatar/{userId}")
    public String deleteAvatar(@PathVariable String userId) {
        try {
            boolean isDeleted = userProfileService.deleteAvatarAndUpdateProfile(userId);
            if (isDeleted) {
                return "Аватар успешно удален!";
            } else {
                return "Аватар не найден для удаления.";
            }
        } catch (Exception e) {
            return "Ошибка при удалении аватара: " + e.getMessage();
        }
    }

    @GetMapping("/avatar/{userId}/{fileName}")
    public byte[] downloadAvatar(@PathVariable String userId, @PathVariable String fileName) throws IOException {
        if (s3Service.avatarExists(userId, fileName)) {
            return s3Service.downloadAvatar(userId, fileName).readAllBytes();
        } else {
            throw new IOException("Аватар не найден.");
        }
    }
}
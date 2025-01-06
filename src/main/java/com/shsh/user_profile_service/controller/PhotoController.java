package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.service.PhotoService;
import com.shsh.user_profile_service.service.S3Service;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("ups/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final S3Service s3Service;
    private final PhotoService photoService;
    @PostMapping("/upload-avatar/{userId}")
    public String uploadAvatar(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        long maxFileSize = 10 * 1024 * 1024; // 10 MB
        if (file.getSize() > maxFileSize) {
            return "Ошибка: Файл слишком большой. Максимальный размер — 10 MB.";
        }

        try {
            return photoService.uploadAvatarAndUpdateProfile(userId, file);
        } catch (IOException e) {
            return "Ошибка загрузки аватара: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Ошибка: " + e.getMessage();
        }
    }
    @DeleteMapping("/delete-avatar/{userId}")
    public String deleteAvatar(@PathVariable String userId) {
        try {
            boolean isDeleted = photoService.deleteAvatarAndUpdateProfile(userId);
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
    @PostMapping("/upload-global-wallpaper/{userId}")
    public String uploadGlobalWallpaper(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        // Ограничение размера файла
        long maxFileSize = 10 * 1024 * 1024; // 10 MB
        if (file.getSize() > maxFileSize) {
            return "Ошибка: Файл слишком большой. Максимальный размер — 10 MB.";
        }

        try {
            return photoService.uploadGlobalWallpaperAndUpdateProfile(userId, file);
        } catch (IOException e) {
            return "Ошибка загрузки обоев: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Ошибка: " + e.getMessage();
        }
    }


    // Удаление глобальных обоев пользователя
    @DeleteMapping("/delete-global-wallpaper/{userId}")
    public String deleteGlobalWallpaper(@PathVariable String userId) {
        try {
            boolean isDeleted = photoService.deleteGlobalWallpaperAndUpdateProfile(userId);
            if (isDeleted) {
                return "Обои успешно удалены!";
            } else {
                return "Обои не найдены для удаления.";
            }
        } catch (Exception e) {
            return "Ошибка при удалении обоев: " + e.getMessage();
        }
    }

    // Получение глобальных обоев пользователя
    @GetMapping("/global-wallpaper/{userId}/{fileName}")
    public byte[] downloadGlobalWallpaper(@PathVariable String userId, @PathVariable String fileName) throws IOException {
        if (s3Service.globalWallpaperExists(userId, fileName)) {
            return s3Service.downloadGlobalWallpaper(userId, fileName).readAllBytes();
        } else {
            throw new IOException("Обои не найдены.");
        }
    }

}
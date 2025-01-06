package com.shsh.user_profile_service.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service
public class S3Service {
    String ACCESS_KEY ="1GGQ99IARPI5XJ0YZGTM";
    String SECRET_KEY ="5DlV4rfBo6VfhDG5BzxmMD1rNVvWB1j2G2YDpY9z";
    String ENDPOINT = "https://s3.ru1.storage.beget.cloud";
    String region = "ru1";
    String BUCKET_NAME = "580647b53aad-shsh-s3";
    private final AmazonS3 s3Client;

    public S3Service() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, "ru1"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .enablePathStyleAccess() // Включаем стиль пути
                .build();
    }

    // Загрузка аватара в S3 (в директорию userAvatars/{userId}/)
    public String uploadAvatar(String userId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String s3Key = "userAvatars/" + userId + "/" + fileName;  // Путь в бакете

        // Проверка на существование старого аватара
        if (doesObjectExist(BUCKET_NAME, s3Key)) {
            deleteAvatarFromS3(userId, fileName); // Удаление старого аватара, если существует
        }

        // Преобразуем MultipartFile в File
        File tempFile = File.createTempFile("avatar_", fileName);
        file.transferTo(tempFile);

        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, s3Key, tempFile);
        s3Client.putObject(request);
        System.out.println("Аватар загружен в S3: " + s3Key);

        // Возвращаем URL на загруженный файл
        return s3Client.getUrl(BUCKET_NAME, s3Key).toString();  // Ссылка на файл
    }

    // Получение аватара из S3 (по ключу userAvatars/{userId}/{fileName})
    public S3ObjectInputStream downloadAvatar(String userId, String fileName) throws IOException {
        String s3Key = "userAvatars/" + userId + "/" + fileName;
        S3Object object = s3Client.getObject(BUCKET_NAME, s3Key);
        return object.getObjectContent();
    }

    // Проверка существования аватара
    public boolean avatarExists(String userId, String fileName) {
        String s3Key = "userAvatars/" + userId + "/" + fileName;
        return s3Client.doesObjectExist(BUCKET_NAME, s3Key);
    }

    // Удаление аватара из S3
    public void deleteAvatarFromS3(String userId, String fileName) {
        String s3Key = "userAvatars/" + userId + "/" + fileName;
        try {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, s3Key));
            System.out.println("Аватар успешно удален из S3: " + s3Key);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении аватара из S3: " + e.getMessage(), e);
        }
    }

    // Загрузка обоев (для всех чатов пользователя) в S3
    public String uploadGlobalWallpaper(String userId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String s3Key = "globalWallpapers/" + userId + "/" + fileName;  // Путь в бакете

        // Проверка на существование старых обоев
        if (doesObjectExist(BUCKET_NAME, s3Key)) {
            deleteGlobalWallpaperFromS3(userId, fileName); // Удаление старых обоев, если существуют
        }

        // Преобразуем MultipartFile в File
        File tempFile = File.createTempFile("wallpaper_", fileName);
        file.transferTo(tempFile);

        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, s3Key, tempFile);
        s3Client.putObject(request);
        System.out.println("Обои для всех чатов пользователя загружены в S3: " + s3Key);

        // Возвращаем URL на загруженный файл
        return s3Client.getUrl(BUCKET_NAME, s3Key).toString();  // Ссылка на файл
    }

    // Получение глобальных обоев пользователя из S3
    public S3ObjectInputStream downloadGlobalWallpaper(String userId, String fileName) throws IOException {
        String s3Key = "globalWallpapers/" + userId + "/" + fileName;
        S3Object object = s3Client.getObject(BUCKET_NAME, s3Key);
        return object.getObjectContent();
    }

    // Проверка существования глобальных обоев
    public boolean globalWallpaperExists(String userId, String fileName) {
        String s3Key = "globalWallpapers/" + userId + "/" + fileName;
        return s3Client.doesObjectExist(BUCKET_NAME, s3Key);
    }

    // Удаление глобальных обоев пользователя из S3
    public void deleteGlobalWallpaperFromS3(String userId, String fileName) {
        String s3Key = "globalWallpapers/" + userId + "/" + fileName;
        try {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, s3Key));
            System.out.println("Обои пользователя успешно удалены из S3: " + s3Key);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении глобальных обоев из S3: " + e.getMessage(), e);
        }
    }

    // Утилита для проверки существования объекта в S3
    private boolean doesObjectExist(String bucketName, String key) {
        try {
            return s3Client.doesObjectExist(bucketName, key);
        } catch (AmazonServiceException e) {
            return false;
        }
    }
}

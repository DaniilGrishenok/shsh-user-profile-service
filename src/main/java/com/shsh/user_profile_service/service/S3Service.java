package com.shsh.user_profile_service.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service
public class S3Service {
    private static final String BUCKET_NAME = "580647b53aad-shsh-s3";
    private static final String ACCESS_KEY = "1GGQ99IARPI5XJ0YZGTM"; // Замените на ваш
    private static final String SECRET_KEY = "5DlV4rfBo6VfhDG5BzxmMD1rNVvWB1j2G2YDpY9z"; // Замените на ваш
    private static final String ENDPOINT = "https://s3.ru1.storage.beget.cloud";

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
    public void deleteAvatarFromS3(String userId, String fileName) {
        String s3Key = "userAvatars/" + userId + "/" + fileName;
        try {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, s3Key));
            System.out.println("Аватар успешно удален из S3: " + s3Key);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении аватара из S3: " + e.getMessage(), e);
        }
    }

}

package com.shsh.user_profile_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.Data;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@RequiredArgsConstructor
public class UserProfile {
    @Id
    private String id;

    @Column(unique = true)
    @NotNull(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть не короче 2 символов и не длиннее 30")
    private String username;

    @Column(unique = true)
    @Email(message = "Email должен быть верного формата")
    @NotNull(message = "Email не может быть null")
    private String email;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate dateOfBirth;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String descriptionOfProfile;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isShshDeveloper = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();
    private LocalDateTime lastUpdated = LocalDateTime.now();

    private String status;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @Size(max = 512, message = "URL аватара слишком длинный")
    @Column(length = 512)
    private String avatarUrl;

    @Size(max = 512, message = "URL обоев слишком длинный")
    @Column(length = 512)
    private String chatWallpaperUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPremium = false;

    private LocalDateTime premiumExpiresAt = null;
    private String nicknameEmoji;
}

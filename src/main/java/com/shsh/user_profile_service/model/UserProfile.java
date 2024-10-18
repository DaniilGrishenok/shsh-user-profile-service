package com.shsh.user_profile_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Data
@RequiredArgsConstructor
public class UserProfile {
    @Id
    private String id;

    @Column(unique = true )
    @NotNull(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть не короче 2 символов и не длиннее 30")
    private String username;
    @NotNull(message = "Email должен быть не null")
    @Email (message = "Email должен быть верного формата")
    private String email;
    private String descriptionOfProfile;
    private String status;
}

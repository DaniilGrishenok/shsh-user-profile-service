package com.shsh.user_profile_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class UserProfile {
    @Id
    private UUID id;

    private String username;
    private String email;
    private String descriptionOfProfile;
    private String status;
}

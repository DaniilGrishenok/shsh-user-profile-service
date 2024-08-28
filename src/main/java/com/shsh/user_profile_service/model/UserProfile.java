package com.shsh.user_profile_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class UserProfile {
    @Id
    private String id;

    @Column(unique = true )
    private String username;
    private String email;
    private String descriptionOfProfile;
    private String status;

}

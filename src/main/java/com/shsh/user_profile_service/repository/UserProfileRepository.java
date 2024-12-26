package com.shsh.user_profile_service.repository;


import com.shsh.user_profile_service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUsername(String username);

    List<UserProfile> findByUsernameContainingIgnoreCase(String substring);
}
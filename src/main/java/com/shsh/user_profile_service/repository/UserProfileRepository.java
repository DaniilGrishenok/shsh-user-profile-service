package com.shsh.user_profile_service.repository;


import com.shsh.user_profile_service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    UserProfile findByUsername(String username);

    List<UserProfile> findByUsernameContainingIgnoreCase(String substring);

}
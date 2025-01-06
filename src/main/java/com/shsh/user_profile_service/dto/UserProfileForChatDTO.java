package com.shsh.user_profile_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileForChatDTO {
    private String username;
    private String avatarUrl;
    private String nicknameEmoji;
    private boolean isPremium;
}

package com.shsh.user_profile_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PremiumStatusResponse {
    private boolean isPremium;
    private LocalDateTime premiumExpiresAt;
}
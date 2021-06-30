package com.github.kbreczko.gitproxy.user.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class UserDto {
    private final Long id;
    private final String login;
    private final String name;
    private final String type;
    private final String avatarUrl;
    private final LocalDateTime createdAt;
    private final BigDecimal calculations;

    public static UserDto of(GithubUserResponse response, BigDecimal calculatedValue) {
        return UserDto.builder()
                .id(response.getId())
                .login(response.getLogin())
                .name(response.getName())
                .type(response.getType())
                .avatarUrl(response.getAvatarUrl())
                .createdAt(response.getCreatedAt())
                .calculations(calculatedValue)
                .build();
    }
}

package com.github.kbreczko.gitproxy.user.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class GithubUserResponse {
    private long id;

    private String login;

    private String name;

    private String type;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private Integer followers;

    private Integer following;

    @JsonProperty("public_repos")
    private Integer publicRepos;
}

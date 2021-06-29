package com.github.kbreczko.gitproxy.user.controllers;

import com.github.kbreczko.gitproxy.common.properties.GitproxyProperties;
import com.github.kbreczko.gitproxy.user.api.UserApi;
import com.github.kbreczko.gitproxy.user.models.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = GitproxyProperties.Mappings.USERS)
public class UserRestController {
    private final UserApi userApi;

    @GetMapping(value = "/{login}")
    public UserDto getUser(@PathVariable String login) {
        return userApi.getUser(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}


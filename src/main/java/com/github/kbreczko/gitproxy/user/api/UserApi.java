package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.user.models.UserDto;
import com.github.kbreczko.gitproxy.user.utils.ResponseCalculatorUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserApi {
    private final UserService userService;
    private final UserGithubService userGithubService;

    public Optional<UserDto> getUser(String login) {
        userService.createOrIncrementRequestCount(login);
        return userGithubService.getUserByLogin(login)
                .map(response -> {
                    final BigDecimal calculatedValue = ResponseCalculatorUtil.calculateValue(response).orElse(null);
                    return UserDto.of(response, calculatedValue);
                });
    }


}

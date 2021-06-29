package com.github.kbreczko.gitproxy.user.utils;

import com.github.kbreczko.gitproxy.user.models.GithubUserResponse;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@UtilityClass
public class ResponseCalculatorUtil {

    public static Optional<BigDecimal> calculateValue(@NonNull GithubUserResponse response) {
        if (response.getFollowers() == null || response.getPublicRepos() == null || response.getFollowers() == 0) {
            return Optional.empty();
        }

        final BigDecimal value = BigDecimal.valueOf(6)
                .setScale(10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(response.getFollowers()), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf((2 + response.getPublicRepos())))
                .setScale(2, RoundingMode.HALF_UP);

        return Optional.of(value);
    }
}

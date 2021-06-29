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
        if (response.getFollowers() == null || response.getPublicRepos() == null) {
            return Optional.empty();
        }

        final int denominator = response.getFollowers() * (2 + response.getPublicRepos());
        if (denominator == 0) {
            return Optional.empty();
        }

        final BigDecimal value = BigDecimal.valueOf(6)
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(denominator), RoundingMode.HALF_DOWN);

        return Optional.of(value);
    }
}

package com.github.kbreczko.gitproxy.user.utils;

import com.github.kbreczko.gitproxy.user.models.GithubUserResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

class ResponseCalculatorUtilTest {

    @Test
    void shouldReturnCalculatedValueSuccessfully() {
        final GithubUserResponse response = GithubUserResponse.builder()
                .id(0L)
                .publicRepos(180)
                .followers(1507)
                .build();

        assertThat(ResponseCalculatorUtil.calculateValue(response))
                .isPresent()
                .get()
                .usingComparator(BIG_DECIMAL_COMPARATOR)
                .isEqualTo(new BigDecimal("0.72"));
    }

    @Test
    void shouldReturnEmptyValueWhenFollowersIsZero() {
        final GithubUserResponse response = GithubUserResponse.builder()
                .id(0L)
                .publicRepos(0)
                .followers(0)
                .build();

        assertThat(ResponseCalculatorUtil.calculateValue(response)).isEmpty();
    }

    @Test
    void shouldReturnEmptyValueWhenResponseIsEmpty() {
        final GithubUserResponse response = GithubUserResponse.builder()
                .id(0L)
                .build();

        assertThat(ResponseCalculatorUtil.calculateValue(response)).isEmpty();
    }
}
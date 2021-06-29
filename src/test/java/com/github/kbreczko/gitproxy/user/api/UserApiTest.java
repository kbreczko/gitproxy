package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import com.github.kbreczko.gitproxy.configurations.AbstractServiceLayerTest;
import com.github.kbreczko.gitproxy.user.models.GithubUserResponse;
import com.github.kbreczko.gitproxy.user.models.UserDto;
import com.github.kbreczko.gitproxy.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserApiTest extends AbstractServiceLayerTest {
    @Autowired
    private UserApi userApi;

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateUserAndSendRequestSuccessfully() {
        //given
        final String login = "test1";
        final GithubUserResponse response = GithubUserResponse.builder()
                .id(0)
                .login(login)
                .name(login)
                .avatarUrl(null)
                .createdAt(LocalDateTime.now())
                .type("User")
                .followers(6)
                .publicRepos(0)
                .build();

        when(restTemplate.exchange(eq("http://localhost:8989/users/" + login), eq(HttpMethod.GET), any(HttpEntity.class), eq(GithubUserResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        //when
        final Optional<UserDto> user = userApi.getUser(login);

        //then
        assertThat(user)
                .isPresent()
                .get()
                .extracting("id", "login", "name", "type", "avatarUrl", "createdAt", "calculations")
                .usingRecursiveFieldByFieldElementComparator()
                .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .containsOnly(response.getId(), login, response.getName(), response.getType(), response.getAvatarUrl(), response.getCreatedAt(), BigDecimal.valueOf(2));

        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .extracting(User::getLogin, User::getRequestCount)
                .containsExactly(login, 1L);
    }

    @Test
    void shouldCreateUserAndReturnEmptyWhenPageNotFound() {
        //given
        final String login = "test1";

        when(restTemplate.exchange(eq("http://localhost:8989/users/" + login), eq(HttpMethod.GET), any(HttpEntity.class), eq(GithubUserResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        //when
        final Optional<UserDto> user = userApi.getUser(login);

        //then
        assertThat(user).isEmpty();

        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .extracting(User::getLogin, User::getRequestCount)
                .containsExactly(login, 1L);
    }
}
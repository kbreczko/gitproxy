package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.user.models.GithubUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserGithubServiceTest {
    private UserGithubService userGithubService;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        this.userGithubService = new UserGithubService(restTemplate, "http://localhost:8989");
    }

    @Test
    void shouldSendRequestSuccessfully() {
        //given
        final String login = "test1";
        final GithubUserResponse expectedResponse = GithubUserResponse.builder()
                .id(0L)
                .login(login)
                .name(login)
                .avatarUrl(null)
                .createdAt(LocalDateTime.now())
                .type("User")
                .followers(6)
                .publicRepos(0)
                .build();

        when(restTemplate.exchange(eq("http://localhost:8989/users/" + login), eq(HttpMethod.GET), any(HttpEntity.class), eq(GithubUserResponse.class)))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        //when
        final Optional<GithubUserResponse> response = userGithubService.getUserByLogin(login);

        //then
        assertThat(response)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        //given
        final String login = "test1";

        when(restTemplate.exchange(eq("http://localhost:8989/users/" + login), eq(HttpMethod.GET), any(HttpEntity.class), eq(GithubUserResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        //when
        final Optional<GithubUserResponse> response = userGithubService.getUserByLogin(login);

        //then
        assertThat(response).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenThrownException() {
        //given
        final String login = "test1";

        when(restTemplate.exchange(eq("http://localhost:8989/users/" + login), eq(HttpMethod.GET), any(HttpEntity.class), eq(GithubUserResponse.class)))
                .thenThrow(new RestClientException("error during sending request"));

        //when
        final Optional<GithubUserResponse> response = userGithubService.getUserByLogin(login);

        //then
        assertThat(response).isEmpty();
    }
}
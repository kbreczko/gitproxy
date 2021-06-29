package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.user.models.GithubUserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static com.github.kbreczko.gitproxy.common.properties.GitproxyProperties.APPLICATION_GITHUB_V3;

@Service
@Slf4j
class UserGithubService {
    private final static String USER_RESOURCE_PATH = "users";

    private final RestTemplate restTemplate;
    private final String githubApi;

    public UserGithubService(RestTemplate restTemplate, @Value("${gitproxy.github.rest.api}") String githubApi) {
        this.restTemplate = restTemplate;
        this.githubApi = githubApi;
    }

    public Optional<GithubUserResponse> getUserByLogin(String login) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, APPLICATION_GITHUB_V3);

        final String uriString = UriComponentsBuilder.fromHttpUrl(githubApi)
                .pathSegment(USER_RESOURCE_PATH, login)
                .toUriString();

        try {
            log.info("Request to Github REST API for login={}", login);
            ResponseEntity<GithubUserResponse> response = restTemplate.exchange(uriString, HttpMethod.GET, new HttpEntity<>(httpHeaders), GithubUserResponse.class);
            log.debug("Response (login={}):\n {} \n {}", login, response, response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (RestClientException ex) {
            log.error("An error occurred during sending request to github rest api", ex);
        }

        return Optional.empty();
    }

}

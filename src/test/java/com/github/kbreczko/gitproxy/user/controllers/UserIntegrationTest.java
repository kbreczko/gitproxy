package com.github.kbreczko.gitproxy.user.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.github.kbreczko.gitproxy.configurations.IntegrationTestConfiguration;
import com.github.kbreczko.gitproxy.user.models.GithubUserResponse;
import com.github.kbreczko.gitproxy.user.models.UserDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

@SpringBootTest(classes = {IntegrationTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        this.wireMockServer = new WireMockRule(8989);
        this.wireMockServer.start();
    }

    @AfterEach
    void destroy() {
        wireMockServer.stop();
    }


    @Test
    void shouldGetUserSuccessfully() throws JsonProcessingException {
        // given
        final long id = 18629689;
        final String login = "kbreczko";
        final String name = "Kamil Breczko";
        final String type = "User";
        final String avatarUrl = "https://avatars.githubusercontent.com/u/18629689?v=4";
        final LocalDateTime createdAt = LocalDateTime.of(2016, 4, 23, 12, 0);

        wireMockServer.stubFor(get(urlEqualTo("/users/" + login))
                .willReturn(okJson(prepareExampleJson(id, login, name, type, avatarUrl, createdAt)))
        );

        // when
        ResponseEntity<UserDto> response = restTemplate.getForEntity("/users/" + login, UserDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .extracting("id", "login", "name", "type", "avatarUrl", "createdAt", "calculations")
                .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .containsExactly(id, login, name, type, avatarUrl, createdAt, new BigDecimal("0.04"));
    }

    private String prepareExampleJson(long id, String login, String name, String type, String avatarUrl, LocalDateTime createdAt) throws JsonProcessingException {
        final Map<String, String> payload = new HashMap<>();
        payload.put("id", String.valueOf(id));
        payload.put("login", login);
        payload.put("name", name);
        payload.put("type", type);
        payload.put("avatar_url", avatarUrl);
        payload.put("public_repos", "25");
        payload.put("followers", "6");
        payload.put("following", "11");
        payload.put("created_at", String.valueOf(createdAt));

        return new ObjectMapper().writeValueAsString(payload);
    }
}
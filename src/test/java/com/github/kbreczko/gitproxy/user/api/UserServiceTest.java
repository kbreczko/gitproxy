package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import com.github.kbreczko.gitproxy.configurations.AbstractServiceLayerTest;
import com.github.kbreczko.gitproxy.configurations.utils.UserMotherObject;
import com.github.kbreczko.gitproxy.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends AbstractServiceLayerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMotherObject userMotherObject;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateNewUser() {
        //given
        final String login = "test1";

        //when
        userService.createOrIncrementRequestCount(login);

        //then
        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .extracting(User::getLogin, User::getRequestCount)
                .containsExactly(login, 1L);
    }

    @Test
    void shouldIncrementRequestCount() {
        //given
        final String login = "test1";
        userMotherObject.createAndSaveUser(login, 1L);

        //when
        userService.createOrIncrementRequestCount(login);

        //then
        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .extracting(User::getLogin, User::getRequestCount)
                .containsExactly(login, 2L);
    }

    @Test
    void shouldCreateNewUserAndIncrementRequestCountConcurrently() {
        //given
        final String login = "test1";

        //when
        IntStream.range(0, 20).parallel().forEach(index -> userService.createOrIncrementRequestCount(login));

        //then
        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .extracting(User::getLogin, User::getRequestCount)
                .containsExactly(login, 20L);
    }
}
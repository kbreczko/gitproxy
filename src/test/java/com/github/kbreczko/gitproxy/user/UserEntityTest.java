package com.github.kbreczko.gitproxy.user;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import com.github.kbreczko.gitproxy.configurations.AbstractServiceLayerTest;
import com.github.kbreczko.gitproxy.configurations.utils.UserMotherObject;
import com.github.kbreczko.gitproxy.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest extends AbstractServiceLayerTest {
    @Autowired
    private UserMotherObject userMotherObject;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnSavedUser() {
        final String login = "test1";
        userMotherObject.createAndSaveUser(login);

        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .extracting(User::getLogin, User::getRequestCount)
                .containsExactly(login, 0L);

    }
}

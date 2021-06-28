package com.github.kbreczko.gitproxy.configurations.utils;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import com.github.kbreczko.gitproxy.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserMotherObject {
    private final UserRepository userRepository;

    @Transactional
    public User createAndSaveUser(String login) {
        return createAndSaveUser(login, 0L);
    }

    @Transactional
    public User createAndSaveUser(String login, long requestCount) {
        User user = new User();
        user.setLogin(login);
        user.setRequestCount(requestCount);
        return userRepository.save(user);
    }
}

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
        User user = new User();
        user.setLogin(login);
        user.setRequestCount(0);
        return userRepository.save(user);
    }
}

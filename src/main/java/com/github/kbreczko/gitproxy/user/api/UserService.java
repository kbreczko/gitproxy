package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import com.github.kbreczko.gitproxy.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createOrIncrementRequestCount(String login) {
        final User user = userRepository.findByLogin(login).orElseGet(() -> createUser(login));
        user.setRequestCount(user.getRequestCount() + 1);
        userRepository.save(user);
    }

    private User createUser(String login) {
        final User user = new User();
        user.setLogin(login);
        user.setRequestCount(0);
        return user;
    }
}

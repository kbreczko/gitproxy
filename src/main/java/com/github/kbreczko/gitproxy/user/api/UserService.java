package com.github.kbreczko.gitproxy.user.api;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import com.github.kbreczko.gitproxy.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
class UserService {
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(value = {CannotAcquireLockException.class}) // maxAttempts = 3, maxDelay = 1s
    public User createOrIncrementRequestCount(String login) {
        final User user = userRepository.findByLogin(login)
                .orElseGet(() -> {
                    log.info("Creating user for login = {}", login);
                    return createUser(login);
                });
        final long nextRequestCount = user.getRequestCount() + 1;
        log.info("Increment request count for login = {}: {} -> {}", login, user.getRequestCount(), nextRequestCount);
        user.setRequestCount(nextRequestCount);
        return userRepository.save(user);
    }

    private User createUser(String login) {
        final User user = new User();
        user.setLogin(login);
        user.setRequestCount(0);
        return user;
    }
}

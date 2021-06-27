package com.github.kbreczko.gitproxy.user.repositories;

import com.github.kbreczko.gitproxy.common.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

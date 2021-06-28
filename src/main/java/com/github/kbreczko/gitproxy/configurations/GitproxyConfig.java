package com.github.kbreczko.gitproxy.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EntityScan(basePackages = {"com.github.kbreczko.gitproxy.common.models"})
@EnableJpaRepositories(basePackages = {"**.repositories"})
@EnableRetry
public class GitproxyConfig {

}

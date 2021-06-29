package com.github.kbreczko.gitproxy.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@Configuration
@EntityScan(basePackages = {"com.github.kbreczko.gitproxy.common.models"})
@EnableJpaRepositories(basePackages = {"**.repositories"})
@EnableRetry
public class GitproxyConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

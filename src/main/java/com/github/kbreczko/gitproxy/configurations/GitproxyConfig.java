package com.github.kbreczko.gitproxy.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"com.github.kbreczko.gitproxy.common.models"})
public class GitproxyConfig {

}

package com.github.kbreczko.gitproxy.configurations;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@TestConfiguration
@Sql(statements = {"DELETE FROM user"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationTestConfiguration {

    @Bean
    public JdbcDatabaseContainer<?> mariaDBContainer() {
        final int connectTimeoutInSeconds = (int) Duration.of(5, ChronoUnit.MINUTES).toSeconds();
        JdbcDatabaseContainer<?> mariaDbContainer = new MariaDBContainer<>(GitproxyTestProperties.DB_DOCKER_IMAGE)
                .withInitScript(GitproxyTestProperties.INIT_SCRIPT_PATH)
                .withDatabaseName(GitproxyTestProperties.DB_NAME)
                .withPassword(GitproxyTestProperties.DB_PASSWORD)
                .withConnectTimeoutSeconds(connectTimeoutInSeconds);
        mariaDbContainer.start();
        return mariaDbContainer;
    }

    @Primary
    @Bean
    public DataSource dataSource(JdbcDatabaseContainer<?> mariaDBContainer) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .username(GitproxyTestProperties.DB_USERNAME)
                .password(mariaDBContainer.getPassword())
                .url(mariaDBContainer.getJdbcUrl())
                .driverClassName(mariaDBContainer.getDriverClassName())
                .build();
    }
}

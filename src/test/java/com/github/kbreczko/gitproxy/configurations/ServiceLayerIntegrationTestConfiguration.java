package com.github.kbreczko.gitproxy.configurations;

import com.github.kbreczko.gitproxy.configurations.utils.UserMotherObject;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@TestConfiguration
@EntityScan(basePackages = {"com.github.kbreczko.gitproxy.common.models"})
@EnableJpaRepositories(basePackages = {"**.repositories"})
@EnableAutoConfiguration()
@ImportAutoConfiguration(value = {
        JpaRepositoriesAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {"**.api"})
@ComponentScan(basePackageClasses = UserMotherObject.class)
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties({DataSourceProperties.class})
@Slf4j
public class ServiceLayerIntegrationTestConfiguration {

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
    @ConfigurationProperties(prefix = "spring.datasource")
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

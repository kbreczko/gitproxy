package com.github.kbreczko.gitproxy.configurations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceLayerTestConfiguration.class})
@Sql(statements = "DELETE FROM user", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class AbstractServiceLayerTest {
}

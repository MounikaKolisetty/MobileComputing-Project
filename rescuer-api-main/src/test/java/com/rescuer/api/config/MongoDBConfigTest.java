package com.rescuer.api.config;

import com.rescuer.api.repository.audit.AuditRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDBConfigTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = RescuePostgresContainer.getInstance();
    @ClassRule
    public static RescueMongoContainer mongoDBContainer = RescueMongoContainer.getInstance();

    @Autowired
    private AuditRepository auditRepository;

    @Test
    public void shouldAbleToStartSession() {
        // Assert
        assertThat(auditRepository.findAll().size()).isGreaterThanOrEqualTo(0);
        assertThat(auditRepository).isNotNull();
    }
}
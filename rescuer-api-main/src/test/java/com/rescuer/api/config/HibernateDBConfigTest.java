package com.rescuer.api.config;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HibernateDBConfigTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = RescuePostgresContainer.getInstance();
    @ClassRule
    public static RescueMongoContainer mongoContainer = RescueMongoContainer.getInstance();
    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldAbleToGetConnection() {
        boolean isConnectionOpen = this.entityManager.isOpen();
        assertThat(isConnectionOpen).isTrue();
    }

}
package com.rescuer.api.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class RescuePostgresContainer extends PostgreSQLContainer<RescuePostgresContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1";
    private static RescuePostgresContainer container;

    public RescuePostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static RescuePostgresContainer getInstance() {
        if(container == null) {
            container = new RescuePostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("POSTGRES_DB_URL", container.getJdbcUrl());
        System.setProperty("POSTGRES_USERNAME", container.getUsername());
        System.setProperty("POSTGRES_PASSWORD", container.getPassword());
    }

}

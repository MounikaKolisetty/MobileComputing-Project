package com.rescuer.api.config;

import org.testcontainers.containers.GenericContainer;

public class RescueMongoContainer extends GenericContainer<RescueMongoContainer> {

    private static final String IMAGE_VERSION = "mongo:4.0";
    public static RescueMongoContainer mongoDBContainer;

    public RescueMongoContainer() {
        super(IMAGE_VERSION);
    }

    public static RescueMongoContainer getInstance() {
        if(mongoDBContainer == null) {
            mongoDBContainer = new RescueMongoContainer();
        }
        return mongoDBContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("mongo.datasource.connectionstring", String.format("mongodb://%s:%s",
                mongoDBContainer.getContainerIpAddress(), mongoDBContainer.getMappedPort(27017)));
        System.setProperty("mongo.datasource.database", "rescuer_db");
    }

}

package com.parent_child.demo;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ComponentScan(basePackages={
        "com.parent_child.demo"})
public class TestcontainersConfiguration {
    private static final String POSTGRES = "postgres";

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withUsername(POSTGRES)
                .withPassword(POSTGRES)
                .withDatabaseName(POSTGRES)
                .withInitScript("init-script.sql");
    }
}

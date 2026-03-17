package com.finplanpro.finplanpro;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinplanproApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinplanproApplication.class, args);
    }

    @Bean
    public FlywayMigrationStrategy repairAndMigrateStrategy() {
        return flyway -> {
            // Repair the checksums
            flyway.repair();
            // Migrate the database
            flyway.migrate();
        };
    }
}

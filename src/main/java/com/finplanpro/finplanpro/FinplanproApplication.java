package com.finplanpro.finplanpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class FinplanproApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinplanproApplication.class, args);
    }

}

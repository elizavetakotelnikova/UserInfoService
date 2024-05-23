package com.example.ownermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@EnableJpaRepositories("com.example.jpa")
@ComponentScan(basePackages = { "com.example.jpa" })
@EntityScan("com.example.jpa.*")
@SpringBootApplication
public class OwnerMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnerMicroserviceApplication.class, args);
    }

}

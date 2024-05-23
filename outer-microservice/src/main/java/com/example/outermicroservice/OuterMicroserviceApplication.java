package com.example.outermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.*")
@ComponentScan(basePackages = { "com.*" })
@EntityScan("com.*")
@SpringBootApplication
public class OuterMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OuterMicroserviceApplication.class, args);
    }

}

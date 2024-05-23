package com.example.ownermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestOwnerMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.from(OwnerMicroserviceApplication::main).with(TestOwnerMicroserviceApplication.class).run(args);
    }

}

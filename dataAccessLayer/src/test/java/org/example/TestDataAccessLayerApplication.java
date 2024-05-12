package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestDataAccessLayerApplication {

    public static void main(String[] args) {
        SpringApplication.from(Main::main).with(TestDataAccessLayerApplication.class).run(args);
    }

}
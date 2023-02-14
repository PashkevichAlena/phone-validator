package com.neotech.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ValidatorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ValidatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
       log.info("Application started. Please go to localhost:8080 for use ui.");
    }
}

package com.codecrafters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UptimeCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(UptimeCheckApplication.class, args);
    }
}

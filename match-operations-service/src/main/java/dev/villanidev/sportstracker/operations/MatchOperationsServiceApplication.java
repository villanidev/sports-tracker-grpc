package dev.villanidev.sportstracker.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "dev.villanidev.sportstracker.model")
@EnableScheduling
public class MatchOperationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchOperationsServiceApplication.class, args);
    }
}

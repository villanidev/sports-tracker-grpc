package dev.villanidev.sportstracker.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "dev.villanidev.sportstracker.model")
@EnableScheduling
public class MatchStreamingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchStreamingServiceApplication.class, args);
    }
}

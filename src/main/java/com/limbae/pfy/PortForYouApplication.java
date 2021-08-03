package com.limbae.pfy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PortForYouApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortForYouApplication.class, args);
    }

}

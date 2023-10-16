package com.chunjae.nest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NestApplication {

    public static void main(String[] args) {
        SpringApplication.run(NestApplication.class, args);
    }

}

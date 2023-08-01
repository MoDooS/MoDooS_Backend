package com.study.modoos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ModoosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModoosApplication.class, args);
    }

}

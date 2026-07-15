package org.example.jwt_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtTestApplication.class, args);
    }

}

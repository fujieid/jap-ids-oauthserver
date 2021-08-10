package com.louie.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lapati5
 */
@SpringBootApplication
public class OauthServiceApplication implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(OauthServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("http://localhost:" + port);
    }
}

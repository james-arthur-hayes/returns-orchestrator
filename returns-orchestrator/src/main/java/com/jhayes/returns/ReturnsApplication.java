package com.jhayes.returns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReturnsApplication {

    public static void main(String[] args) {
        // This is the command that starts the Netty server (WebFlux)
        // and wires up all the @Service and @RestController classes.
        SpringApplication.run(ReturnsApplication.class, args);
    }
}
package com.spring.ecommercesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class ECommerceSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceSystemApplication.class, args);
    }

}

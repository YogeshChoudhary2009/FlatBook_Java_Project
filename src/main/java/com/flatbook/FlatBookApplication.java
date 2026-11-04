package com.flatbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlatBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlatBookApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  FlatBook - Online Flat Booking System Started");
        System.out.println("  Access: http://localhost:8080");
        System.out.println("=================================================");
    }
}

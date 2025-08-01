package com.example.cubesat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class CubesatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CubesatApplication.class, args);
    }

}

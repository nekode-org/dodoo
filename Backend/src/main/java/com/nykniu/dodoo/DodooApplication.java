package com.nykniu.dodoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DodooApplication {

    public static void main(String[] args) {
        GlobalVars.connectionManager = new ConnectionManager();
        GlobalVars.connectionManager.setupConnections();

        SpringApplication.run(DodooApplication.class, args);
    }

}
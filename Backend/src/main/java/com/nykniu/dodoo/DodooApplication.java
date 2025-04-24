package com.nykniu.dodoo;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nykniu.dodoo.manager.ConnectionManager;

@SpringBootApplication
public class DodooApplication {

    public static void main(String[] args) {
        GlobalVars.connectionManager = new ConnectionManager();
        GlobalVars.managers = new HashMap<>();
        GlobalVars.managers.put("mariadb", "org.mariadb.jdbc.Driver");
        GlobalVars.managers.put("mysql", "com.mysql.cj.jdbc.Driver");

        SpringApplication.run(DodooApplication.class, args);
    }

}
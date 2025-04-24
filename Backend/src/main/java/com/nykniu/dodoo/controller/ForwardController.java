package com.nykniu.dodoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {
    
    @RequestMapping(value = "/Datos")
    public String forward() {
        return "forward:/Datos/index.html";
    }
}
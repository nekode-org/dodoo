package com.nykniu.dodoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {
    
    @RequestMapping(value = "/Datos")
    public String forwardDatos() {
        return "forward:/Datos/index.html";
    }
    @RequestMapping(value = "/Aviso")
    public String forwardAviso() {
        return "forward:/Aviso/index.html";
    }
    @RequestMapping(value = "/Recu")
    public String forwardRecu() {
        return "forward:/Recu/index.html";
    }
}
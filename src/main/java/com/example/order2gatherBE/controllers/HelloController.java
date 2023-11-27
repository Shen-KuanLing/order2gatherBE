package com.example.order2gatherBE.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping(path="/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
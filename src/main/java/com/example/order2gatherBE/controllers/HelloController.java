package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.OrderEventModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

// import org.json.simple.*;
import org.json.simple.JSONObject;

import javax.validation.Valid;
// import org.json.*;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value="/")
public class HelloController {
    int counter=0;
    public ResponseEntity<String> createHello (){
        return new ResponseEntity<String>("Hello", HttpStatus.OK);
    }
}


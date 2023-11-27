package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.LoginRequest;
import com.example.order2gatherBE.services.AuthenticationService;
import jakarta.validation.Valid;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, String>> login(
        @Valid @RequestBody LoginRequest request
    ) {
        String jwt = authenticationService.login(
            request.getEmail(),
            request.getUsername()
        );
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("jwt", jwt);
        return ResponseEntity.ok(res);
    }
}

package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.AuthenticationRequest;
import com.example.order2gatherBE.services.AuthenticationService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> login(
        @Valid @RequestBody AuthenticationRequest.Login request
    ) {
        String jwt = authenticationService.login(request.getAccessToken());
        if (jwt == null) {
            return ResponseEntity.badRequest().build();
        }
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("jwt", jwt);
        return ResponseEntity.ok(res);
    }
}

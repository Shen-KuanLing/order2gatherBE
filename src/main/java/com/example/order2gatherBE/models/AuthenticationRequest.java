package com.example.order2gatherBE.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class AuthenticationRequest {
    static public class Login {

        @NotBlank(message = "The accessToken is required.")
        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }
    }
}

package com.example.order2gatherBE.util;
import com.example.order2gatherBE.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ControllerAuthorization {
    public static ResponseEntity<?> verifyAndResponse(String token, AuthenticationService authenticationService) {
        String bearerToken = extract(token);
        int verifiedUId = authenticationService.verify(bearerToken);
        if (verifiedUId == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return null; // Return null to indicate successful verification
    }
    public static String extract(String originToken) {
        if (originToken != null && originToken.startsWith("Bearer ")) {
            // Extract the token part (excluding "Bearer ")
            return originToken.substring(7);
        }
        return originToken;
    }
}

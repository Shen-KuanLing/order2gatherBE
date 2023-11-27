package com.example.order2gatherBE.services;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;

@Configuration
@Service
public class AuthenticationService {
    @Autowired
    AuthenticationRepository authenticationRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRE_TIME = 60 * 60 * 1000; // 60 minutes

    private static Map<String, Object> readJSONtoMap(String jsonString) {
        if (jsonString == null || jsonString.trim().length() == 0) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            return mapper.readValue(jsonString, typeRef);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Map<String, Object> decodeAccessToken(String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return readJSONtoMap(response.body());
    }

    public String login(String accessToken) {

        try {
            // decode Access Token
            Map<String, Object> data;
            data = decodeAccessToken(accessToken);
            String gmail = (String) data.get("email");
            String username = (String) data.get("name");

            // get uid
            UserModel user = authenticationRepository.findUserbyGmail(gmail);
            if (user == null) {
                authenticationRepository.InsertUser(gmail, username);
                user = authenticationRepository.findUserbyGmail(gmail);
            }
            return createAccessToken(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createAccessToken(int uid) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        long now = System.currentTimeMillis();

        // the payload will be {uid: xxx, iat: yyy, exp: zzz}
        return Jwts.builder()
                .claim("uid", uid)
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRE_TIME))
                .signWith(key) // default is HS256
                .compact();

    }

    /*
     * If valid, it will return the UID; otherwise, it will return -1.
     */
    public int verify(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Claims payload = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return (int) payload.get("uid");
        } catch (JwtException e) {
            return -1;
        }
    }
}

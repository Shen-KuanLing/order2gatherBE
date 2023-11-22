package com.example.order2gatherBE.services;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.example.order2gatherBE.models.LoginRequest;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;

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

    public String login(String gmail, String username) {
        // get uid
        UserModel user = authenticationRepository.findUserbyGmail(gmail);
        if (user == null) {
            authenticationRepository.InsertUser(gmail, username);
            user = authenticationRepository.findUserbyGmail(gmail);
        }
        return createAccessToken(user.getId());
    }

    private String createAccessToken(int uid) {
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

package com.example.order2gatherBE.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.order2gatherBE.models.FriendRequest;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.FriendService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/friend", produces = MediaType.APPLICATION_JSON_VALUE)
public class FriendController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    FriendService friendService;

    @PostMapping("/add")
    public ResponseEntity<HashMap<String, Boolean>> add(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody FriendRequest.Add req) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        HashMap<String, Boolean> res = new HashMap<String, Boolean>();
        res.put("status", friendService.add(uid, req.getEmail(), req.getNickname()));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/delete")
    public ResponseEntity<HashMap<String, Boolean>> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody FriendRequest.Delete req) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        HashMap<String, Boolean> res = new HashMap<String, Boolean>();
        res.put("status", friendService.delete(uid, req.getFid()));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/removeUserFromGroup")
    public ResponseEntity<HashMap<String, Boolean>> removeUserFromGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody FriendRequest.RemoveUserFromGroup req) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        HashMap<String, Boolean> res = new HashMap<String, Boolean>();
        res.put("status", friendService.removeUserFromGroup(uid, req.getFid(), req.getGid()));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/get")
    public ResponseEntity<HashMap<String, Object>> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(friendService.get(uid));
    }

    @GetMapping("/getGroupInfo")
    public ResponseEntity<HashMap<String, Object>> getGroupInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam int id) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(friendService.getGroupInfo(uid, id));
    }

    @PostMapping("/createGroup")
    public ResponseEntity<Map<String, Integer>> createGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody FriendRequest.CreateGroup req) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        HashMap<String, Integer> res = new HashMap<String, Integer>();
        res.put("gid", friendService.createGroup(uid, req.getName()));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/addUsersToGroup")
    public ResponseEntity<Map<String, Boolean>> addUsersToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestBody FriendRequest.AddFriendToGroup req) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        HashMap<String, Boolean> res = new HashMap<String, Boolean>();
        res.put("status", friendService.addUsersToGroup(uid, req.getFids(), req.getGid()));
        return ResponseEntity.ok(res);
    }
}

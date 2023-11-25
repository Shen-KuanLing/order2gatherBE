package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.exceptions.DataAccessException;
import com.example.order2gatherBE.exceptions.ResponseEntityException;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

import static com.example.order2gatherBE.util.RestaurantFormatGenerator.*;

@RestController
@RequestMapping(value = "/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    AuthenticationService authenticationService;
    private HashMap<String, Object> formatMap = new HashMap<>();


    // Display Restaurant
    @GetMapping(value = "/display", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRestaurants(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "rid", required = false) String rid) throws DataAccessException, ResponseEntityException
    {
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": Please provided the correct jwt.}");

        String message = "";
        formatMap.clear();
        // Get Restaurant Detail or List
        if (rid != null) {
                formatMap = restaurantService.getRestaurantDetail(Integer.parseInt(rid), uid);
                message = "Get Restaurant Detail";
        } else{
                formatMap = restaurantService.getRestaurantList(uid);
                message = "Get Restaurant List";
        }
        String formatStr="";

        HttpStatus code = HttpStatus.OK;
        if(formatMap.isEmpty()) {
            message = "No Restaurant Detail";
            formatStr = generateFormat(message);
            code = HttpStatus.BAD_REQUEST;
        }else {
            formatStr = generateFormat(message, formatMap);
        }

        return ResponseEntity
                            .status(code)
                            .body(formatStr);
    }


    // Delete the restaurant
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteRestaurantDetail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "rid")int rid) throws DataAccessException {

        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": Please provided the correct jwt.}");

        int success = restaurantService.deleteRestaurant(rid, uid);
        if(success == 0)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("{\"message\": \"Please refrain from deleting information about others' restaurants\"}");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("{\"message\":\" The %s Restaurant has been deleted.\"}", rid));

    }

    // Save Restaurant.
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postRestaurantDetail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestParam("restaurant") String rest,
            @RequestParam(value = "menu", required = false) MultipartFile[] images)
            throws DataAccessException, ResponseEntityException
    {
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": Please provided the correct jwt.}");


        String message = "";
        List<String> fileNames = new ArrayList<String>();

        RestaurantModel restModel = toModule(rest);
        restModel.setUid(uid);

        int rid = restaurantService.saveRestaurant(restModel);
        restModel.setId(rid);
        if(images != null) {
                System.out.println("Pass restaurant");
                Arrays.asList(images).stream().forEach(image -> {
                    restaurantService.saveImage(restModel.getId(), image);
                    fileNames.add(image.getOriginalFilename());
                });
        }
        message = "Uploaded the files successfully: " + fileNames;
        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(String.format("{\"status\": %d, \"message\": %s }",0,  message));

    }

    // Update Restaurant
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putRestaurantDetail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("restaurant") String rest,
            @RequestParam(value = "menu", required = false) MultipartFile[] images)
            throws DataAccessException, ResponseEntityException
    {

        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1)
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": Please provided the correct jwt.}");

        //Prepare Response Format
        String message = "";
        List<String> fileNames = new ArrayList<String>();
        //Give the restaurant Module and set user id ;
        RestaurantModel restModel = toModule(rest);
        restModel.setUid(uid);

        //Update the restaurant Information

        int updateCounts = restaurantService.updateRestaurant(restModel);
        HttpStatus code = HttpStatus.OK;
        if(images != null && updateCounts != 0) {
            Arrays.asList(images).stream().forEach(image -> {
                        restaurantService.saveImage(restModel.getId(), image);
                        fileNames.add(image.getOriginalFilename());
            });
            message = "Update the restaurant or images successfully: " + fileNames;
        }else{
            if(updateCounts == 0)
                message = "Fail to update restaurant or image, because the restaurant is deleted or didn't exist! ";
            else
                message = "Update the restaurant successfully";
        }

        return ResponseEntity
                    .status(code)
                    .body(String.format("{\"message\": \"%s\" }", message));

    }

}

package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.RestaurantService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.json.*;
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

    Map<String, Object> response = new HashMap<>();

    // Display Restaurant
    @GetMapping(value = "/display", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRestaurants(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "rid", required = false) String rid)
    {
        response.clear();
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1 ) {
            response.put("message", "Please provided the correct jwt");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
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

        HttpStatus code = HttpStatus.OK;
        DisplayResponse displayResponse = new DisplayResponse();
        if(formatMap.isEmpty()) {
            message = "No Restaurant Detail";
            displayResponse.setMessage(message);
            code = HttpStatus.FORBIDDEN;
        }else {
            //Generate Response

            displayResponse.setMessage(message);
            if(formatMap.containsKey("menu"))
                displayResponse.setMenu((List<String>) formatMap.get("menu"));

            if(formatMap.containsKey("food"))
                displayResponse.setFood((List<FoodModel>) formatMap.get("food"));
            if(formatMap.containsKey("restaurant"))
                displayResponse.setRestaurant((List<RestaurantModel>) formatMap.get("restaurant"));
        }

        return ResponseEntity
                .status(code)
                .body(displayResponse);
    }


    // Delete the restaurant
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRestaurantDetail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "rid")int rid) {

        response.clear();
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1 ) {
            response.put("message", "Please provided the correct jwt");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
        int success = restaurantService.deleteRestaurant(rid, uid);
        //Generate Response
        if(success == 0)
            response.put("message", "Please refrain from deleting information about others' restaurants");
        else
            response.put("message", String.format("The %s Restaurant has been deleted.", rid));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
    @DeleteMapping(value = "/deleteFood", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFood(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(value = "rid")int rid,
            @RequestParam(value = "fid")int foodId) {

        response.clear();
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1 ) {
            response.put("message", "Please provided the correct jwt");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
        int success = restaurantService.deleteFood(rid, foodId);
        //Generate Response
        if(success == 0)
            response.put("message", "Please refrain from deleting information about others' foods.");
        else
            response.put("message", String.format("The %s food has been deleted.", rid));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    // Save Restaurant.
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postRestaurantDetail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @Valid @RequestParam("restaurant") String rest,
            @Valid @RequestParam("food") String foods,
            @RequestParam(value = "menu", required = false) MultipartFile[] images)
    {
        response.clear();
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1 ) {
            response.put("message", "Please provided the correct jwt");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
        String message = "";
        List<String> fileNames = new ArrayList<String>();

        RestaurantModel restModel = toModule(rest);
        restModel.setUid(uid);

        int rid = restaurantService.saveRestaurant(restModel);
        restModel.setId(rid);
        // Collect Foods
        List<FoodModel> foodsList = Arrays.asList(toFood(foods, rid));
        for(FoodModel foodModel : foodsList) {
            System.out.println(foodModel);
            foodModel.setRid(rid);
        }
        // Saving Food
        restaurantService.savefoods(foodsList);

        if(images != null) {
            Arrays.asList(images).stream().forEach(image -> {
                restaurantService.saveImage(restModel.getId(), image);
                fileNames.add(image.getOriginalFilename());
            });
        }


        message = "Uploaded the files successfully: " + fileNames;

        response.put("message", message);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    // Update Restaurant
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putRestaurantDetail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam("restaurant") String rest,
            @RequestParam(value = "menu", required = false) MultipartFile[] images)
    {

        response.clear();
        //Authorized
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if(uid == -1) {
            response.put("message", "Please provided the correct jwt");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

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
        response.put("message", message);
        return ResponseEntity
                .status(code)
                .body(response);

    }


    @Getter
    @Setter
    @AllArgsConstructor
    class DisplayResponse{
        String message;
        @JsonFormat
        List<RestaurantModel> restaurant;
        @JsonFormat
        List<FoodModel> food;
        @JsonFormat
        List<String> menu;
        DisplayResponse(){
            message = "";
            restaurant = new ArrayList<>();
            food = new ArrayList<>();
            menu = new ArrayList<>();
        }
    }
}
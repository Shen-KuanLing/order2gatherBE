package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.RestaurantModel;
import com.example.order2gatherBE.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import com.example.order2gatherBE.util.RestaurantFormatGenerator;

@RestController
@RequestMapping(value = "/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    @Autowired
    RestaurantService restaurantService;


    private HashMap<String, Object> formatMap = new HashMap<>();

    @GetMapping("/test")
    public String display() {
        return "For test";
    }


    /*
     *  @apiName getRestaurants
     *  @apiParam {Number} uid user id
     *  @apiSuccess {Number} status 0: 成功, 1: 失敗
     *  @apiSuccessExample {json} Success-Response:
     *  {
     *      "status": 0,
     *      "data": {
     *          "restaurant": [
     *              {
     *                  "id" : 19120211814,
     *                  "uid" : 82113114,
     *                  “name"： "Saturn",
     *                  "address": "拉尼亞凱亞－本超星系團－室女座星系團－本星系群－銀河系－太陽系－土星",
     *                  "phone": "123456789",
     *                  "isDelete": 0,
     *                  "openHour" : "I open at the close",
     *              }
     *          ],
     *      },
     *  }
     */
    @GetMapping(value = "/getRestaurants/{uid}")
    public ResponseEntity<String> getRestaurants(@PathVariable int uid) {

        List<RestaurantModel> restList = restaurantService.getRestaurantList(uid);

        try {
            String format = RestaurantFormatGenerator.generateFormat(0, "[ ]");
            if (!restList.isEmpty()) {
                formatMap.clear();
                formatMap.put("restaurants", restList);
                format = RestaurantFormatGenerator.generateFormat(0, formatMap);
            }
            return new ResponseEntity<String>(format, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(RestaurantFormatGenerator.generateFormat(1, "[ ]"), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getRestaurant/{rid}")
    public ResponseEntity<String> getRestaurantDetail(@PathVariable int rid) {

        formatMap = restaurantService.getRestaurantDetail(rid);

        try {
            String format = RestaurantFormatGenerator.generateFormat(0, formatMap);
            return new ResponseEntity<String>(format, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(RestaurantFormatGenerator.generateFormat(1, "[ ]"), HttpStatus.OK);
        }
    }


}

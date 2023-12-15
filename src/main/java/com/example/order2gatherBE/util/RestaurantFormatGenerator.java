package com.example.order2gatherBE.util;

import com.example.order2gatherBE.exceptions.ResponseEntityException;
import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantFormatGenerator {

    // Convert json format response
    public static RestaurantModel toModule(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, RestaurantModel.class);
        } catch (Exception e) {
            throw new ResponseEntityException(
                "Failed to build Request Entity",
                e.getMessage()
            );
        }
    }

    public static FoodModel[] toFood(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, FoodModel[].class);
        } catch (Exception e) {
            throw new ResponseEntityException(
                "Failed to build Request Entity",
                e.getMessage()
            );
        }
    }
}

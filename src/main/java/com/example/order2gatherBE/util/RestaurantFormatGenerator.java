package com.example.order2gatherBE.util;
import com.example.order2gatherBE.exceptions.ResponseEntityException;
import com.example.order2gatherBE.models.RestaurantModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class RestaurantFormatGenerator {

    //Convert obj 2 json format string
    private static String jsonify(Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr="" ;
        try {
            if(obj == null)
                return "";
            jsonStr = objectMapper.writeValueAsString(obj);
        }catch (Exception e) {
            throw new ResponseEntityException("Failed to Contruct Response Entity", e.getMessage());
        }
        return jsonStr;
    }
    // Convert json format response
    public static RestaurantModel toModule(String json)  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, RestaurantModel.class);
        }catch (Exception e) {
            throw new ResponseEntityException("Failed to build Request Entity", e.getMessage());
        }
    }


    //Overloading
    public static String generateFormat(String message, HashMap<String, Object> objMap) throws ResponseEntityException{
        String jsonFormat = String.format("{\"message\":\"%s\", ",message);
        for(String name:  objMap.keySet()){
            if(name.equals("menu")){
                jsonFormat += String.format(  "\"%s\": %s, " , name, objMap.get(name));
            }else{
                jsonFormat += String.format(  "\"%s\": %s, " , name,jsonify(objMap.get(name)));
            }
        }

        jsonFormat += "}";
        return jsonFormat;
    }

    //Overloading
    public static String generateFormat(String message) throws ResponseEntityException{
        return String.format("{\"message\": \"%s\" }",  message);
    }
}

package com.example.order2gatherBE.util;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class RestaurantFormatGenerator {

    private static String jsonify(Object obj) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonStr = objectMapper.writeValueAsString(obj);
        System.out.println(jsonStr);
        return jsonStr;
    }
    public static String generateFormat(int status, String dataFormat){
        String jsonFormat = String.format("{\"status\":%d, \"data\": {\"restaurants\" : %s } }", status, dataFormat);
        System.out.println("Serialize Module "+jsonFormat);
        return jsonFormat;
    }

    public static String generateFormat(int status, HashMap<String, Object> objMap) throws Exception{
        String jsonFormat = String.format("{\"status\":%d, \"data\": { ", status);
        for(String infoName: objMap.keySet()){
            jsonFormat += String.format("\"%s\": %s ,", infoName,
                    jsonify(objMap.get(infoName)));
        }
        jsonFormat += " } }";
        System.out.println(jsonFormat);
        return jsonFormat;
    }
}

package com.example.order2gatherBE.services;

import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;
import com.example.order2gatherBE.repository.FoodRepository;
import com.example.order2gatherBE.repository.RestaurantImageRepository;
import com.example.order2gatherBE.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

@Configuration
@Service
public class RestaurantService {

    @Autowired
    AuthenticationRepository authenticationRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantImageRepository restaurantImageRepository;
    @Autowired
    FoodRepository foodRepository;

    public void createRestaurant(RestaurantModel restaurant, List<RestaurantImageModel> menuImages){
        System.out.println("Want 2 create restaurant Whole Information");


    }

    public List<RestaurantModel> getRestaurantList(int userID)
    {
        System.out.println("Want to get "+userID+"'s restaurant List.");
        return restaurantRepository.findByUId(userID);
    }

    public HashMap<String, Object> getRestaurantDetail(int rid)
    {
        RestaurantModel rest = restaurantRepository.getRestDetail(rid);
        List<RestaurantImageModel> images = restaurantImageRepository.getMenus(rid);
        List<FoodModel> foods = foodRepository.getFoods(rid);

        HashMap<String, Object> objMap = new HashMap<String, Object>();

        objMap.put("restaurants", rest);
        objMap.put("menus", images);
        objMap.put("foods", foods);
        return objMap;
    }

}

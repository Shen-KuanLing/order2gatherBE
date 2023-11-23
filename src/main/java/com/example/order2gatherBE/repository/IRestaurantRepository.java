package com.example.order2gatherBE.repository;
import com.example.order2gatherBE.models.RestaurantModel;

import java.util.List;

public interface IRestaurantRepository {

    public RestaurantModel getRestDetail(int restaurantID);


    public void save(RestaurantModel restaurantModel);
    public void update(RestaurantModel restaurantModel);

    public List<RestaurantModel> findByUId(Integer userID);
    public int deleteByName(String restaurantName);
    public int deleteById(Integer restaurantId);
}

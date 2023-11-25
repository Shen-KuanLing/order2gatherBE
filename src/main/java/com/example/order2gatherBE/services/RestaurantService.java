package com.example.order2gatherBE.services;

import com.example.order2gatherBE.exceptions.DataAccessException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public int saveRestaurant(RestaurantModel restaurant) throws DataAccessException{
        restaurantRepository.save(restaurant);
        return  restaurantRepository.getTableSize();
    }

    public int updateRestaurant(RestaurantModel restaurant){

        return restaurantRepository.update(restaurant);
    }
    public HashMap<String, Object> getRestaurantList(int userID) throws DataAccessException
    {
        List<RestaurantModel> listRest = restaurantRepository.findByUId(userID);
        HashMap<String, Object> objMap = new HashMap<String, Object>();
        objMap.put("restaurants", listRest);
        return objMap;
    }

    public  HashMap<String, Object> getRestaurantDetail(int rid,int uid) throws RuntimeException
    {
        HashMap<String, Object> objMap = new HashMap<String, Object>();

        RestaurantModel rest = restaurantRepository.getRestDetail(rid, uid);
        // Null return empty map
        if(rest == null)
            return objMap;
        List<RestaurantImageModel> images = null;
        System.out.println("pass");
        if (!rest.getIsDelete())
                // Using the upload image
                images = restaurantImageRepository.get(rid);

        List<FoodModel> foods = foodRepository.getFoods(rid);

        objMap.put("restaurant", rest);
        objMap.put("food", foods);

        //Construct Image base64 response
        String imageformat = "[ ";
        for(RestaurantImageModel image: images)
            imageformat += image.toString()+" ,";
        imageformat += "]";
        //Using the upload image
        objMap.put("menu", imageformat);

        return objMap;
    }


    public void saveImage(int rid, MultipartFile images) throws DataAccessException{
        RestaurantImageModel restaurantImageModel = new RestaurantImageModel();
        try {
            restaurantImageModel.setMenuImage(images.getBytes());
            restaurantImageModel.setRId(rid);
            String output = restaurantImageRepository.save(restaurantImageModel);
            System.out.println("Save Image "+ output);
        }catch(IOException e){
            System.out.println("Save Image Failed!");
            throw new DataAccessException(500, "Fail to transfer Byte array ", e.getMessage());
        }
    }

    public int deleteRestaurant(int rid, int uid) throws DataAccessException{
        return restaurantRepository.deleteById(rid, uid);
    }
}

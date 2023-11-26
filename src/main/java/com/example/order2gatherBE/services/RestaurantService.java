package com.example.order2gatherBE.services;

import com.example.order2gatherBE.exceptions.DataAccessException;
import com.example.order2gatherBE.exceptions.ForbiddenException;
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

    // Save Restaurant by restaurant Model
    public int saveRestaurant(RestaurantModel restaurant){
        int result = restaurantRepository.save(restaurant);
        return  restaurantRepository.getSaveRestaurantID();
    }


    // Update Restaurant List by user ID
    public int updateRestaurant(RestaurantModel restaurant){

        return restaurantRepository.update(restaurant);
    }
    // Get Restaurant List by user ID
    public HashMap<String, Object> getRestaurantList(int userID)
    {
        List<RestaurantModel> listRest = restaurantRepository.findByUId(userID);
        HashMap<String, Object> objMap = new HashMap<String, Object>();
        objMap.put("restaurants", listRest);
        return objMap;
    }

    public  HashMap<String, Object> getRestaurantDetail(int rid,int uid)
    {
        HashMap<String, Object> objMap = new HashMap<String, Object>();

        RestaurantModel rest = restaurantRepository.getRestDetail(rid, uid);
        // Null return empty map
        if(rest == null)
            return objMap;
        List<RestaurantImageModel> images = null;
        //System.out.println("pass");
        if (!rest.getIsDelete())
                // Using the upload image
                images = restaurantImageRepository.get(rid);

        List<FoodModel> foods = foodRepository.getFoods(rid);


        //Construct Image base64 response
        String imageformat = "[ ";
        for(RestaurantImageModel image: images)
            imageformat += image.toString()+" ,";
        imageformat += "]";
        //Using the upload image

        //Generate the response
        objMap.put("restaurant", rest);
        objMap.put("food", foods);
        objMap.put("menu", imageformat);

        return objMap;
    }

    //Save Image with restaurant ID
    public void saveImage(int rid, MultipartFile images){
        RestaurantImageModel restaurantImageModel = new RestaurantImageModel();
        try {
            restaurantImageModel.setMenuImage(images.getBytes());
            restaurantImageModel.setRId(rid);
            String output = restaurantImageRepository.save(restaurantImageModel);
            //System.out.println("Save Image "+ output);
        }catch(IOException e){
            //System.out.println("Save Image Failed!");
            throw new DataAccessException(500, "Fail to transfer Byte array ", e.getMessage());
        }
    }
    //Delete Restaurant by restaurant ID
    public int deleteRestaurant(int rid, int uid){
        return restaurantRepository.deleteById(rid, uid);
    }

}

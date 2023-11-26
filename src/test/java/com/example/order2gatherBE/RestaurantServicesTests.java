package com.example.order2gatherBE;

import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.*;
import com.example.order2gatherBE.services.FriendService;
import com.example.order2gatherBE.services.RestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Assertions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class RestaurantServicesTests {

    @Autowired
    private RestaurantService restaurantService;
    @MockBean
    private RestaurantRepository restaurantRepository;
    @MockBean
    private RestaurantImageRepository restaurantImageRepository;
    @MockBean
    private FoodRepository foodRepository;


    RestaurantModel createRestaurant(Integer uid,String name ,boolean isDelete) {
         RestaurantModel rest = new RestaurantModel();
         rest.setUid(uid);
         rest.setName(name);
         rest.setIsDelete(isDelete);
         rest.setAddress("Test Address");
         rest.setPhone("123456789");
         return  rest;
    }

    RestaurantImageModel createMenu(int rid,byte[] menu)
    {
         return new RestaurantImageModel(menu, rid);
    }

    @Test
    @DisplayName(value="[UNIT TEST]: Save restaurant to DB. ")
    void saveRestaurant() throws Exception {
        RestaurantModel restModel = createRestaurant(2, "Rest 1",false);

        // Assume restaurant table is empty
        // Assume the db always work and it will return restaurant
        Mockito.when(restaurantRepository.save(restModel)).thenReturn(1);
        Mockito.when(restaurantRepository.getSaveRestaurantID()).thenReturn(1);

        // Test Add Restaurant
        Assertions.assertEquals(restaurantService.saveRestaurant(restModel), 1);
    }



    @Test
    @DisplayName(value="[UNIT TEST]: Get restaurant list by user id.")
    void getRestaurantList() throws Exception{
        RestaurantModel restModel1 = createRestaurant(2, "Rest 1",false);
        RestaurantModel restModel2 = createRestaurant(2,"Rest 2" ,false);
        RestaurantModel restModel3 = createRestaurant(2, "Rest 3",false);
        List<RestaurantModel> restList = new ArrayList<>();
        restList.add(restModel1);
        restList.add(restModel2);
        restList.add(restModel3);
        // Get Restaurant List by user ID
        Mockito.when(restaurantRepository.findByUId(2 )).thenReturn(restList);

        // Because getRestaurantList -> HashMap<String, Object>
        HashMap<String, Object> results = restaurantService.getRestaurantList(2);


        List<RestaurantModel> resultsRest = (List<RestaurantModel>) results.get("restaurants");
        //Test List Restaurant 3
        Assertions.assertEquals(resultsRest.size() , 3);

        //Test Restaurant 2
        Assertions.assertEquals(resultsRest.get(1).getName() , "Rest 2");
    }


    //To-Do Get Restaurant Detail
    @Test
    void getRestaurantInfo() throws  Exception{
        RestaurantModel restModel1 = createRestaurant(2, "Rest 1",false);
        RestaurantImageModel menu = createMenu(3, new byte[]{3} );
        // TO-Do
    }

    @Test
    @DisplayName(value="[UNIT TEST]: Delete Restaurant by user id and restaurant id.")
    void removeRestaurant() throws Exception{
        int UID = 2;
        int RID = 3;
        Mockito.when(restaurantRepository.deleteById(3, 2)).thenReturn(1);

        // Test Delete by
        Assertions.assertEquals( restaurantService.deleteRestaurant(3, 2), 1);

    }

}

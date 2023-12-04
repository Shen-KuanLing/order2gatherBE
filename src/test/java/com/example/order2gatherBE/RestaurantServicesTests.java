package com.example.order2gatherBE;

import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.*;
import com.example.order2gatherBE.services.FriendService;
import com.example.order2gatherBE.services.RestaurantService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

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

    private final int SUCCESS = 1;
    private static List<RestaurantModel> restaurantModels = new ArrayList<>();

    RestaurantModel createRestaurant(Integer uid,String name ,boolean isDelete ) {
         RestaurantModel rest = new RestaurantModel();
         rest.setUid(uid);
         rest.setName(name);
         rest.setIsDelete(isDelete);
         rest.setAddress("[UNIT TEST]");
         rest.setPhone("123456789");
         return  rest;
    }
    RestaurantImageModel createMenu(int rid,byte[] menu)
    {
        return new RestaurantImageModel(menu, rid);
    }

    @BeforeEach
    void initRestaurant(){
        restaurantModels.add(createRestaurant(2023, "軟體工程", false));
        restaurantModels.add(createRestaurant(2023, "第五組", false));
        restaurantModels.add(createRestaurant(2023, " A+ ", false));
    }

    @Test
    @DisplayName(value="[UNIT TEST]: Save restaurant to DB. ")
    void saveRestaurant() throws Exception {

        List<Integer> counts = new ArrayList<>();
        // Assume restaurant table is empty
        // Assume the db always work and it will return restaurant
        for(RestaurantModel rest: restaurantModels){
            Mockito.when(restaurantService.saveRestaurant(rest)).thenReturn(1);
            counts.add(restaurantService.saveRestaurant(rest));
        }

        // Test Add Restaurant
        for(int success: counts){
            Assertions.assertEquals(success, SUCCESS);
        }
    }



    @Test
    @DisplayName(value="[UNIT TEST]: Get restaurant list by user id.")
    void getRestaurantList() throws Exception{
        // Get Restaurant List by user ID
        Mockito.when(restaurantRepository.findByUId(2023)).thenReturn(restaurantModels);

        // Because getRestaurantList -> HashMap<String, Object>
        HashMap<String, Object> results = restaurantService.getRestaurantList(2023);
        System.out.println(results);

        List<RestaurantModel> resultsRest = (List<RestaurantModel>) results.get("restaurant");
        //Test List Restaurant 3
        Assertions.assertEquals(resultsRest.size() , 3*3);

        //Test Restaurant 2
        Assertions.assertEquals(resultsRest.get(1).getName() , "第五組");
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

    @AfterAll
    @DisplayName(value="[UNIT TEST]: Check Expected Results.")
    public static void checkModels(){
        Assertions.assertEquals(restaurantModels.size(), 3*4);
    }

}

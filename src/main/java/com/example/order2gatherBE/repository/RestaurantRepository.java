package com.example.order2gatherBE.repository;


import com.example.order2gatherBE.models.RestaurantModel;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;


@Repository
public class RestaurantRepository implements  IRestaurantRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public RestaurantModel getRestDetail(int restaurantID)  {
        String sql = "Select * FROM restaurant WHERE Id = ? and isDelete = 0";
        RestaurantModel restaurantModel = null;
        restaurantModel = jdbcTemplate.queryForObject(sql,
                    new Object[] {restaurantID},
                    new int[] {Types.INTEGER},
                    new BeanPropertyRowMapper<RestaurantModel>(RestaurantModel.class) );

        return restaurantModel;
    }

    @Override
    public void save(RestaurantModel restaurantModel)  {
        String sql = "INSERT INTO restaurant(uid, name, address, phone, isDelete, openHour) Values(?, ?, ?, ?, ?, ?)";

    }

    @Override
    public void update(RestaurantModel restaurantModel) {
        //String sql = "Update INTO";
    }

    @Override
    public List<RestaurantModel> findByUId(Integer userID) {
        String sql = "Select * FROM restaurant WHERE uid = ? and isDelete = 0";
        List<RestaurantModel> rests = null;

        rests = this.jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<RestaurantModel>(RestaurantModel.class), userID );

        return rests;
    }

    @Override
    public int deleteByName(String restaurantName) {

        String sql = "UPDATE restaurant SET isDelete = ? WHERE name = ?";
        return this.jdbcTemplate.update(sql, new Object[] {1, restaurantName });

    }

    @Override
    public int deleteById(Integer restaurantId) {
        String sql = "UPDATE restaurant SET isDelete = ? WHERE id = ?";
        return this.jdbcTemplate.update(sql, new Object[] {1, restaurantId });
    }

}

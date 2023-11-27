package com.example.order2gatherBE.repository;

import com.example.order2gatherBE.exceptions.DataAccessException;
import com.example.order2gatherBE.models.RestaurantModel;
import java.sql.Types;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get Restaurant Detail
    public RestaurantModel getRestDetail(int restaurantID, int uid) {
        String sql =
            "Select * FROM restaurant WHERE Id = ? and uid = ? and isDelete = 0 ;";
        List<RestaurantModel> restaurantList = null;
        RestaurantModel restaurantModel = null;
        try {
            restaurantList =
                this.jdbcTemplate.query(
                        sql,
                        new BeanPropertyRowMapper<RestaurantModel>(
                            RestaurantModel.class
                        ),
                        restaurantID,
                        uid
                    );
            if (!restaurantList.isEmpty()) restaurantModel =
                restaurantList.get(0);
        } catch (Exception e) {
            throw new DataAccessException(
                404,
                "[SQL EXCEPTION]: Get Image Failed. Please check is deleted or not. ",
                e.getMessage()
            );
        }
        return restaurantModel;
    }

    // Add Restaurant Information
    public int save(RestaurantModel restaurantModel) {
        String sql =
            "INSERT INTO restaurant(uid, name, address, phone, isDelete, openHour) Values(?, ?, ?, ?, ?, ?);";
        try {
            return this.jdbcTemplate.update(
                    sql,
                    new Object[] {
                        restaurantModel.getUid(),
                        restaurantModel.getName(),
                        restaurantModel.getAddress(),
                        restaurantModel.getPhone(),
                        0,
                        restaurantModel.getOpenHour(),
                    }
                );
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to save Restaurant",
                e.getMessage()
            );
        }
    }

    // Update Restaurant Information
    public int update(RestaurantModel restaurantModel) {
        String sql = String.format(
            "UPDATE Restaurant SET name=\"%s\", address=\"%s\", phone=\"%s\", openHour=\"%s\" WHERE id=%d AND uid=%d;",
            restaurantModel.getName(),
            restaurantModel.getAddress(),
            restaurantModel.getPhone(),
            restaurantModel.getOpenHour(),
            restaurantModel.getId(),
            restaurantModel.getUid()
        );
        try {
            return this.jdbcTemplate.update(sql);
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to update Restaurant",
                e.getMessage()
            );
        }
    }

    public List<RestaurantModel> findByUId(Integer userID) {
        String sql = "Select * FROM restaurant WHERE uid = ? and isDelete = 0";
        try {
            return this.jdbcTemplate.query(
                    sql,
                    new BeanPropertyRowMapper<RestaurantModel>(
                        RestaurantModel.class
                    ),
                    userID
                );
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to find Restaurant List by user id",
                e.getMessage()
            );
        }
    }

    public int deleteByName(String restaurantName) {
        String sql = "UPDATE restaurant SET isDelete = ? WHERE name = ?";
        return this.jdbcTemplate.update(
                sql,
                new Object[] { 1, restaurantName }
            );
    }

    public int deleteById(Integer restaurantId, int uid) {
        String sql =
            "UPDATE restaurant SET isDelete = ? WHERE id = ? and uid = ?";
        try {
            return this.jdbcTemplate.update(
                    sql,
                    new Object[] { 1, restaurantId, uid }
                );
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to delete Restaurant by restaurant id",
                e.getMessage()
            );
        }
    }

    public int getSaveRestaurantID() {
        try {
            return this.jdbcTemplate.queryForObject(
                    "Select count(*) FROM Restaurant",
                    Integer.class
                );
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to get Restaurant Table Size",
                e.getMessage()
            );
        }
    }
}

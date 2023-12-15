package com.example.order2gatherBE.repository;



import com.example.order2gatherBE.exceptions.DataAccessException;
import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
@Repository

public class RestaurantImageRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get Menu Image
    public List<RestaurantImageModel> get(int rid){
        String sql = "Select * FROM restaurantImage WHERE rid = ?";
        List<RestaurantImageModel> images = null;
        //images = this.jdbcTemplate.queryForList(sql,RestaurantImageModel.class );
        try {
            images = (List<RestaurantImageModel>) this.jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<RestaurantImageModel>(RestaurantImageModel.class), rid);
        }catch (Exception e){
            throw new DataAccessException(500, "[SQL EXCEPTION]: Fail to get menu image", e.getMessage());
        }
        return images;
    }

    // Save Menu Image
    public String save(RestaurantImageModel menu){

        String sql = "Insert INTO restaurantImage(rid, menuImage) VALUES (?, ?)  ";
        try {

            this.jdbcTemplate.update(
                    sql,
                    new Object[] {
                            menu.getRId(),
                            new SqlLobValue(menu.getMenuImage(), new DefaultLobHandler())},
                    new int[] {Types.INTEGER, Types.BLOB});

            return "Success";
        }catch (Exception e){
            throw new DataAccessException(500, "[SQL EXCEPTION]: Fail to save Images", e.getMessage());
        }
    }
}

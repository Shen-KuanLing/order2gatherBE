package com.example.order2gatherBE.repository;



import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantImageModel;
import com.example.order2gatherBE.models.RestaurantModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
@Repository

public class RestaurantImageRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RestaurantImageModel> getMenus(int rid){

        String sql = "Select * FROM restaurantImage WHERE rid = ? ";
        List<RestaurantImageModel> images = null;
        images = this.jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<RestaurantImageModel>(RestaurantImageModel.class), rid );
        return images;
    }


    public String save(List<RestaurantImageModel> images){

        String sql = "Insert * FROM food(rid, images) VALUES (?, ?)  ";
        try {
            this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {

                    RestaurantImageModel image = images.get(i);
                    ps.setInt(1, image.getRId());
                    ps.setBlob(2, new java.io.ByteArrayInputStream(image.getImage()), Types.BLOB);
                }
                @Override
                public int getBatchSize() {
                    return images.size();
                }
            });
            return "Success";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed";
        }
    }
}

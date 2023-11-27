package com.example.order2gatherBE.repository;

import com.example.order2gatherBE.exceptions.DataAccessException;
import com.example.order2gatherBE.models.FoodModel;
import com.example.order2gatherBE.models.RestaurantModel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FoodRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<FoodModel> getFoods(int rid) {
        String sql = "Select * FROM food WHERE rid = ? ";
        List<FoodModel> foods = null;
        try {
            foods =
                this.jdbcTemplate.query(
                        sql,
                        new BeanPropertyRowMapper<FoodModel>(FoodModel.class),
                        rid
                    );
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to get foods",
                e.getMessage()
            );
        }
        return foods;
    }

    // Not use it
    public String save(List<FoodModel> foods) {
        String sql = "Insert * FROM food(rid, name, price) VALUES (?, ?, ?)  ";
        try {
            this.jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                            FoodModel food = foods.get(i);
                            ps.setInt(1, food.getRid());
                            ps.setString(2, food.getName());
                            ps.setInt(3, food.getPrice());
                        }

                        @Override
                        public int getBatchSize() {
                            return foods.size();
                        }
                    }
                );
            return "Success";
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to save foods",
                e.getMessage()
            );
        }
    }
}

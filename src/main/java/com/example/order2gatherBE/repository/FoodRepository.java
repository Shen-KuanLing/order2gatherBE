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
        String sql = "Insert INTO food(rid, name, price) VALUES (?, ?, ?)  ";
        try {
            int[] SuccessCnt =
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
            int total = 0;
            for (int i = 0; i < SuccessCnt.length; i++) {
                total += SuccessCnt[i];
            }
            if (total == foods.size()) {
                return "All foods are saved successfully.";
            } else {
                return "Some foods couldn't be saved because they didn't exist.";
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to save foods",
                e.getMessage()
            );
        }
    }

    public String update(List<FoodModel> foods) {
        String sql =
            "UPDATE food SET name=?, price=? WHERE id = ? and rid = ? ";
        try {
            int[] successCnt =
                this.jdbcTemplate.batchUpdate(
                        sql,
                        new BatchPreparedStatementSetter() {

                            @Override
                            public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                                FoodModel food = foods.get(i);
                                ps.setString(1, food.getName());
                                ps.setInt(2, food.getPrice());
                                ps.setInt(3, food.getId());
                                ps.setInt(4, food.getRid());
                            }

                            @Override
                            public int getBatchSize() {
                                return foods.size();
                            }
                        }
                    );

            int total = 0;
            for (int i = 0; i < successCnt.length; i++) {
                total += successCnt[i];
            }
            if (total == foods.size()) {
                return "All foods are saved successfully.";
            } else {
                return "Some foods couldn't be saved because they didn't exist.";
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to save foods",
                e.getMessage()
            );
        }
    }

    public int deleteById(int restaurantId, int fid) {
        String sql = "DELETE FROM food WHERE id = ? and rid = ?";
        try {
            return this.jdbcTemplate.update(
                    sql,
                    new Object[] { fid, restaurantId }
                );
        } catch (Exception e) {
            throw new DataAccessException(
                500,
                "[SQL EXCEPTION]: Fail to delete Restaurant by food id and restaurant id",
                e.getMessage()
            );
        }
    }
}

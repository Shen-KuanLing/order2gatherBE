package com.example.order2gatherBE.repository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.UserModel;

import java.util.List;;

@Repository
public class AuthenticationRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * Return UserModel Object, if not exist return null
     */
    public UserModel findUserbyGmail(String gmail) {
        String sql = "Select * from user where gmail = ?";
        List<UserModel> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserModel.class), gmail);
        if (users.size() == 0)
            return null;
        return users.get(0);
    }

    /*
     * Return UserModel Object, if not exist return null
     */
    public UserModel findUserbyId(int uid) {
        String sql = "Select * from user where id = ?";
        List<UserModel> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserModel.class), uid);
        if (users.size() == 0)
            return null;
        return users.get(0);
    }

    /*
     * Insert user info by giving gmail and username. Please check that there
     * doesn't exist the same gmail user before insertion.
     */
    public int InsertUser(String gmail, String username) {
        Date now = new Date();
        String sql = "INSERT INTO user (gmail, username, lastLogin) VALUES(?,?,?);";
        return jdbcTemplate.update(sql, gmail, username, now);
    }
}

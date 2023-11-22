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
     * Return user Object, if not exist return -1
     */
    public UserModel findUserbyGmail(String gmail) {
        try {
            String sql = "Select * from user where gmail = ?";
            List<UserModel> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserModel.class), gmail);
            return users.get(0);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    /*
     * Return user Object, if not exist return -1
     */
    public UserModel findUserbyId(int uid) {
        try {
            String sql = "Select * from user where id = ?";
            UserModel user = jdbcTemplate.queryForObject(sql, UserModel.class, uid);
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

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

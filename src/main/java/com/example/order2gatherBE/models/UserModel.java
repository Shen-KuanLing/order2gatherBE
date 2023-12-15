package com.example.order2gatherBE.models;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

// @Entity
// @Table(name = "user")
public class UserModel {
    private int id;
    @NotNull
    private String gmail;
    @NotNull
    private String username;
    @NotNull
    private Timestamp lastLogin;

    public String getGmail() {
        return gmail;
    }

    public int getId() {
        return id;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

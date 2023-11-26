package com.example.order2gatherBE.models;

public class FriendModel {
    private int user1;
    private int user2;
    private String user2NickName;

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public String getUser2NickName() {
        return user2NickName;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public void setUser2NickName(String user2NickName) {
        this.user2NickName = user2NickName;
    }
}

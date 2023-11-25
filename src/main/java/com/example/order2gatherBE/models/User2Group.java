package com.example.order2gatherBE.models;

public class User2Group {
    private int gid;
    private int uid;
    private int role;

    public int getGid() {
        return gid;
    }

    public int getRole() {
        return role;
    }

    public int getUid() {
        return uid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}

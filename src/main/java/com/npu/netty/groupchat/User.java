package com.npu.netty.groupchat;

public class User {
    private int id;
    private String pwd;

    public User(int id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

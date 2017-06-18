package com.example.root.poalabmanager.models;

import java.io.Serializable;

/**
 * Created by minatto on 18/06/17.
 */

public class Projects implements Serializable{
    private int id;
    private String name;
    private int user;

    public Projects(int _id,String name,int user) {
        this.id = _id;
        this.name = name;
        this.user = user;
    }

    public Projects(String name,int user) {
        this.name = name;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}

package com.example.root.poalabmanager.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Created by minatto on 18/06/17.
 */

public class Projects implements Serializable{
    private int id;
    private String name;
    private int user;
    private int hash;

    public Projects(int _id,String name,int user,int hash) {
        this.id = _id;
        this.name = name;
        this.user = user;
        this.hash = hash;
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

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Projects{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", hash=" + hash +
                '}';
    }
}

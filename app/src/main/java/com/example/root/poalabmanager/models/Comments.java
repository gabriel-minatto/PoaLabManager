package com.example.root.poalabmanager.models;

/**
 * Created by minatto on 04/07/17.
 */

public class Comments {

    public int user;
    public int project;
    public String text;

    public Comments() {}

    public Comments(int user,int project) {
        this.user = user;
        this.project = project;
    }

    public Comments(int user,int project,String text) {
        this.user = user;
        this.project = project;
        this.text = text;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "user=" + user +
                ", project=" + project +
                ", text='" + text + '\'' +
                '}';
    }
}

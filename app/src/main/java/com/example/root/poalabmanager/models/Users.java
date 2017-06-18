package com.example.root.poalabmanager.models;

/**
 * Created by minatto on 17/06/17.
 */

public class Users{
    private Integer id;
    private String login;
    private String senha;
    /**
     * @param id
     * @param login
     * @param senha
     */
    public Users(Integer id, String login, String senha){
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    public Users(String login,String senha) {
        this.login = login;
        this.senha = senha;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }
}

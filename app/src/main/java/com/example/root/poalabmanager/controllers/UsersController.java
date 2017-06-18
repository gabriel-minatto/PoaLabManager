package com.example.root.poalabmanager.controllers;

import android.content.Context;

import com.example.root.poalabmanager.dao.UsersDao;
import com.example.root.poalabmanager.models.Users;

import java.util.List;

/**
 * Created by minatto on 17/06/17.
 */

public class UsersController {

    private static UsersDao usuarioDao;
    private static UsersController instance;

    public static UsersController getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new UsersController();
            usuarioDao = new UsersDao(context);
        }
        return instance;
    }

    public boolean insert(Users user) throws Exception {
        if (user == null || user.getLogin().isEmpty() || user.getSenha().isEmpty()) {
            return false;
        }
        usuarioDao.insert(user);
        return true;
    }
    public void update(Users usuario) throws Exception {
        usuarioDao.update(usuario);
    }
    public List<Users> findAll() throws Exception {
        return usuarioDao.findAll();
    }

    public void deleteAll() throws Exception {
        usuarioDao.deleteAll();
    }

    public boolean validaLogin(String login, String senha) throws Exception {
        Users user = usuarioDao.findByLogin(login, senha);
        if (user == null || user.getLogin() == null || user.getSenha() == null) {
            return false;
        }
        String informado = login + senha;
        String esperado = user.getLogin() + user.getSenha();
        if (informado.equals(esperado)) {
            return true;
        }
        return false;
    }
}

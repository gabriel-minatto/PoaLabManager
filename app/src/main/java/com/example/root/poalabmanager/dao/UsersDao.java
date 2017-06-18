package com.example.root.poalabmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.root.poalabmanager.BDUtil;
import com.example.root.poalabmanager.models.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minatto on 17/06/17.
 */

public class UsersDao extends BDUtil {

    private final String TABLE = "users";

    public UsersDao(Context context) {
        super(context);
    }

    public void insert(Users usuario) throws Exception {
        ContentValues values = new ContentValues();
        values.put("login", usuario.getLogin());
        values.put("senha", usuario.getSenha());
        getDatabase().insert(TABLE, null, values);
    }

    public void update(Users usuario) throws Exception {
        ContentValues values = new ContentValues();
        values.put("login", usuario.getLogin());
        values.put("senha", usuario.getSenha());
        getDatabase().update(TABLE, values, "id = ?", new String[] { "" + usuario.getId() });
    }

    public void deleteAll() throws Exception {
        getDatabase().delete(TABLE, null, null);
    }

    public Users findById(Integer id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE _id = ?";
        String[] selectionArgs = new String[] { "" + id };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        return montaUsuario(cursor);
    }

    public List<Users> findAll() throws Exception {
        List<Users> retorno = new ArrayList<Users>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaUsuario(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public Users montaUsuario(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Integer id = cursor.getInt(cursor.getColumnIndex("_ID"));
        String login = cursor.getString(cursor.getColumnIndex("LOGIN"));
        String senha = cursor.getString(cursor.getColumnIndex("SENHA"));
        return new Users(id, login, senha);
    }

    /**
     * @param login
     * @param senha
     * @return
     */
    public Users findByLogin(String login, String senha) {
        String sql = "SELECT * FROM " + TABLE + " WHERE login = ? AND senha = ?";
        String[] selectionArgs = new String[] { login, senha };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        return montaUsuario(cursor);
    }
}

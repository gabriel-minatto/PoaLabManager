package com.example.root.poalabmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.root.poalabmanager.BDUtil;
import com.example.root.poalabmanager.models.Projects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minatto on 18/06/17.
 */

public class ProjectsDao extends BDUtil {

    private final String TABLE = "projects";

    public ProjectsDao(Context context) {
        super(context);
    }

    public void insert(Projects project) throws Exception {
        ContentValues values = new ContentValues();
        values.put("name", project.getName());
        values.put("user", project.getUser());
        getDatabase().insert(TABLE, null, values);
    }

    public void update(Projects project) throws Exception {
        ContentValues values = new ContentValues();
        values.put("name", project.getName());
        values.put("user", project.getUser());
        getDatabase().update(TABLE, values, "_ID = ?", new String[] { "" + project.getId() });
    }

    public void deleteAll() throws Exception {
        getDatabase().delete(TABLE, null, null);
    }

    public void deleteById(int id) throws  Exception{
        ContentValues values = new ContentValues();
        getDatabase().delete(TABLE,"_ID = ?",new String[] { "" + id });
    }

    public Projects findById(Integer id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE _id = ?";
        String[] selectionArgs = new String[] { "" + id };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        return montaProjeto(cursor);
    }

    public List<Projects> findAll() throws Exception {
        List<Projects> retorno = new ArrayList<Projects>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaProjeto(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public List<Projects> findByUser(int user) throws Exception {
        List<Projects> retorno = new ArrayList<Projects>();
        String sql = "SELECT * FROM " + TABLE + " WHERE USER ="+user;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaProjeto(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public Projects montaProjeto(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Integer id = cursor.getInt(cursor.getColumnIndex("_ID"));
        String name = cursor.getString(cursor.getColumnIndex("NAME"));
        int user = cursor.getInt(cursor.getColumnIndex("USER"));
        return new Projects(id, name, user);
    }

    /**
     * @param name
     * @return
     */
    public Projects findByName(String name) {
        String sql = "SELECT * FROM " + TABLE + " WHERE name = ?";
        String[] selectionArgs = new String[] { name };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        return montaProjeto(cursor);
    }


}

package com.example.root.poalabmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.root.poalabmanager.utils.BDUtil;
import com.example.root.poalabmanager.models.Projects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by minatto on 18/06/17.
 */

public class ProjectsDao extends BDUtil {

    private final String TABLE = "projects";

    public ProjectsDao(Context context) {
        super(context);
    }

    public Projects insert(Projects project) throws Exception {
        long id;
        int hash = geraHash(project);
        ContentValues values = new ContentValues();
        values.put("name", project.getName());
        values.put("user", project.getUser());
        values.put("hash", hash);
        id = getDatabase().insert(TABLE, null, values);
        if(id != -1){
            project.setId((int)id);
            project.setHash(hash);
        }
        return project;
    }

    public void update(Projects project) throws Exception {
        ContentValues values = new ContentValues();
        values.put("name", project.getName());
        values.put("user", project.getUser());
        values.put("hash", project.getHash());
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
        int hash = cursor.getInt(cursor.getColumnIndex("HASH"));
        return new Projects(id, name, user, hash);
    }

    public int geraHash(Projects project){
        SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return Objects.hashCode(project.getName()+project.getUser()+m_sdf.format(new Date()));
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

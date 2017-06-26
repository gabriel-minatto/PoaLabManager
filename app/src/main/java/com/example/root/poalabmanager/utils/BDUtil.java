package com.example.root.poalabmanager.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by minatto on 17/06/17.
 */

public class BDUtil extends SQLiteOpenHelper {

    private static final String BASE_DE_DADOS = "POALABMANAGER.sqlite";
    private static final int VERSAO = 3;
    protected SQLiteDatabase database;

    //Tables
    private String[] dbTables = {
            "CREATE TABLE IF NOT EXISTS users (_ID INTEGER PRIMARY KEY AUTOINCREMENT, LOGIN TEXT NOT NULL UNIQUE, SENHA TEXT NOT NULL)", //usersTable
            "CREATE TABLE IF NOT EXISTS projects (_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL , USER INTEGER, HASH TEXT NOT NULL UNIQUE,FOREIGN KEY (USER) REFERENCES users(_ID))", //projectsTable
            "CREATE TABLE IF NOT EXISTS comments (_ID INTEGER PRIMARY KEY AUTOINCREMENT, COMMENT TEXT NOT NULL, PROJECT INTEGER,FOREIGN KEY (PROJECT) REFERENCES projects(_ID))" //commentsTable
    };
    //private String usersTable = "CREATE TABLE users (_ID INTEGER PRIMARY KEY AUTOINCREMENT, LOGIN TEXT NOT NULL UNIQUE, SENHA TEXT NOT NULL)";
    //private String projectsTable = "CREATE TABLE projects (_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL UNIQUE, USER INTEGER,FOREIGN KEY (USER) REFERENCES users(_ID))";
    //private String commentsTable = "CREATE TABLE comments (_ID INTEGER PRIMARY KEY AUTOINCREMENT, COMMENT TEXT NOT NULL, PROJECT INTEGER,FOREIGN KEY (PROJECT) REFERENCES projects(_ID))";

    public BDUtil(Context context) {
        super(context, BASE_DE_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //StringBuilder criarTabela = new StringBuilder();
        //criarTabela.append("CREATE TABLE users (");
        //criarTabela.append(" _ID   INTEGER PRIMARY KEY AUTOINCREMENT, ");
        //criarTabela.append(" LOGIN TEXT NOT NULL,");
        //criarTabela.append(" SENHA TEXT NOT NULL)");
        /*db.execSQL(this.usersTable);
        db.execSQL(this.projectsTable);
        db.execSQL(this.commentsTable);*/
        for(int i = 0; i < this.dbTables.length; i++){
            db.execSQL(this.dbTables[i]);
        }
    }

    public SQLiteDatabase getDatabase() {
        if (this.database == null) {
            this.database = getWritableDatabase();
        }
        return this.database;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE projects");
            for (int i = 0; i < this.dbTables.length; i++) {
                db.execSQL(this.dbTables[i]);
            }
        }
    }


}

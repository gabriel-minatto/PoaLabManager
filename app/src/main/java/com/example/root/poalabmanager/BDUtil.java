package com.example.root.poalabmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by minatto on 17/06/17.
 */

public class BDUtil extends SQLiteOpenHelper {

    private static final String BASE_DE_DADOS = "POALABMANAGER.sqlite";
    private static final int VERSAO = 1;
    protected SQLiteDatabase database;

    public BDUtil(Context context) {
        super(context, BASE_DE_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder criarTabela = new StringBuilder();
        criarTabela.append("CREATE TABLE users (");
        criarTabela.append(" _ID   INTEGER PRIMARY KEY AUTOINCREMENT, ");
        criarTabela.append(" LOGIN TEXT NOT NULL,");
        criarTabela.append(" SENHA TEXT NOT NULL)");
        db.execSQL(criarTabela.toString());
    }

    public SQLiteDatabase getDatabase() {
        if (this.database == null) {
            this.database = getWritableDatabase();
        }
        return this.database;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

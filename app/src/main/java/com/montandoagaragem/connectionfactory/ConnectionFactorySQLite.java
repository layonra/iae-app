package com.montandoagaragem.connectionfactory;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ConnectionFactorySQLite extends SQLiteOpenHelper {



    public static final String NOME_BD = "TabelaServicos.db";
    public static final int VERSAO_BD = 13;
    public static final String TABLE_NAME_SERVICO = "tabela_servico";
    public static final String[] COLUMNS_SERVICO = {"_id", "SERVICO", "IP", "PORTA"};


    public ConnectionFactorySQLite(Context context) {
        super(context, NOME_BD, null, VERSAO_BD, new DatabaseErrorHandler() {

            @Override
            public void onCorruption(SQLiteDatabase dbObj) {
                dbObj.close();
            }
        });
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        String sql;

        sql = "CREATE TABLE tabela_servico(_id integer primary key autoincrement, SERVICO TEXT, IP TEXT, PORTA INT)";

        bd.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {

        bd.execSQL("DROP TABLE IF EXISTS " + ConnectionFactorySQLite.TABLE_NAME_SERVICO);
        onCreate(bd);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

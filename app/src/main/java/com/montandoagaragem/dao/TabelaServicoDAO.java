package com.montandoagaragem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.montandoagaragem.connectionfactory.ConnectionFactorySQLite;
import com.montandoagaragem.entity.TabelaServico;
import com.montandoagaragem.util.ServicoInexistenteException;

/**
 * Created by Emerson Oliveira on 27/09/15 at 16:21.
 *
 */
public class TabelaServicoDAO {

    private SQLiteDatabase connection;

    public void getConnectionInstance(Context context) {
        if (this.connection == null) {
            ConnectionFactorySQLite iniDB = new ConnectionFactorySQLite(context);
            this.connection = iniDB.getWritableDatabase();
        }
    }

    public void inserir(TabelaServico tabelaServico) {

        ContentValues cv = new ContentValues();

        cv.put("SERVICO", tabelaServico.getServico());
        cv.put("IP", tabelaServico.getIp());
        cv.put("PORTA", tabelaServico.getPorta());

        this.connection.insert(ConnectionFactorySQLite.TABLE_NAME_SERVICO, null, cv);
        this.connection.close();
    }

    public void editar (TabelaServico tabelaServico) {

        ContentValues cv = new ContentValues();

        cv.put("SERVICO", tabelaServico.getServico());
        cv.put("IP", tabelaServico.getIp());
        cv.put("PORTA", tabelaServico.getPorta());

        this.connection.update(ConnectionFactorySQLite.TABLE_NAME_SERVICO, cv, "SERVICO = ?",
                new String[]{String.valueOf(tabelaServico.getServico())});
        this.connection.close();

    }

    public TabelaServico buscar(String servico) throws ServicoInexistenteException {

        TabelaServico tabelaServico = new TabelaServico();
        Cursor c;

        c = this.connection.query(ConnectionFactorySQLite.TABLE_NAME_SERVICO, ConnectionFactorySQLite.COLUMNS_SERVICO,
                "SERVICO = '" + servico + "'",
                null, null, null, null);

        Log.i("UPE", "Numero de colunas: " + c.getCount());

        c.moveToFirst();

        if (c.getCount() > 0) {

            tabelaServico.setId(c.getLong(0));
            tabelaServico.setServico(c.getString(1));
            tabelaServico.setIp(c.getString(2));
            tabelaServico.setPorta(c.getInt(3));

            Log.i("UPE", tabelaServico + "");

        } else {
            throw new ServicoInexistenteException();
        }

        c.close();
        this.connection.close();
        this.connection = null;

        return tabelaServico;
    }
}

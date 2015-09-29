package com.montandoagaragem.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.montandoagaragem.R;
import com.montandoagaragem.entity.Usuario;

public class Consulta extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView  listview;
    private AdapterListView adapterListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemClickListener(this);
        createListView();
    }

    //Metodo que chama algo que irá parecer no listView
    //Ex: uma consulta SQL
    private void createListView(){
        //Cria o adapter
        //No Construtor do adapter é passado por parametro o contexto e um List de usuario

        // adapterListView = new AdapterListView(); --> descomentar depois
        listview.setAdapter(adapterListView);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuario usuario = adapterListView.getItem(position);

        Toast.makeText(this, usuario.getTelefone(),Toast.LENGTH_SHORT).show();
    }
}

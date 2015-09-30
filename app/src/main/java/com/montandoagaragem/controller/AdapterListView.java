package com.montandoagaragem.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.montandoagaragem.R;
import com.montandoagaragem.entity.Usuario;

import java.util.ArrayList;
import java.util.List;


public class AdapterListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Usuario> usuario;

    public AdapterListView(Context context, List<Usuario> usuario)
    {
        //Itens que preencheram o listview
        this.usuario = usuario;

        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }


    public int getCount()
    {
        return usuario.size();
    }


    public Usuario getItem(int position)
    {
        return usuario.get(position);
    }


    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        //Pega o item de acordo com a posição.
        Usuario u = usuario.get(position);

        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.item_listview, null);

        ((TextView) view.findViewById(R.id.txt_nome)).setText(u.getNome());
        ((TextView) view.findViewById(R.id.txt_cidade)).setText(u.getEmail());
        ((TextView) view.findViewById(R.id.txt_instrumento)).setText(u.getInstrumento());

        return view;
    }

}

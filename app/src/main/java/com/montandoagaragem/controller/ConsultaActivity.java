package com.montandoagaragem.controller;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.montandoagaragem.R;
import com.montandoagaragem.dao.TabelaServicoDAO;
import com.montandoagaragem.entity.TabelaServico;
import com.montandoagaragem.entity.Usuario;
import com.montandoagaragem.util.ServicoInexistenteException;
import com.montandoagaragem.util.SocketManagement;
import com.montandoagaragem.util.TimeoutThread;
import com.montandoagaragem.util.URL;
import com.montandoagaragem.util.UsuarioInexistenteException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static int count = 0;

    private List<Usuario> usuarios;

    private ListView  listview;
    private AdapterListView adapterListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setTitle(getResources().getString(R.string.app_name));
        }


        listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemClickListener(this);

        this.usuarios = new ArrayList<>();

        try {
            TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

            tabelaServicoDAO.getConnectionInstance(getApplicationContext());

            TabelaServico ts = tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_C);

            gerarLista(ts);

        } catch (ServicoInexistenteException e) {
            gerarLista();
        }
    }

    //Metodo que chama algo que irá parecer no listView
    //Ex: uma consulta SQL
    private void createListView(){
        //Cria o adapter
        //No Construtor do adapter é passado por parametro o contexto e um List de usuario

         adapterListView = new AdapterListView(getApplicationContext(), usuarios);
        listview.setAdapter(adapterListView);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuario usuario = adapterListView.getItem(position);

        Toast.makeText(this, usuario.getTelefone(),Toast.LENGTH_SHORT).show();
    }

    private void gerarLista () {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String data;
                try {
                    TimeoutThread timeoutThread = new TimeoutThread();

                    Thread t = new Thread(timeoutThread);

                    t.start();
                    data = SocketManagement.sendDataUDP(URL.PROCESSO.PROCESSO_C, URL.IP.IP_DNS, URL.PORTA.PORTA_DNS);

                    Log.i("UPE", data);

                    String[] s = data.split(":");
                    String ip = s[0];
                    String p = s[1];
                    int porta = Integer.parseInt(p);

                    TabelaServico ts = new TabelaServico(URL.PROCESSO.PROCESSO_C, ip, porta);

                    TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                    tabelaServicoDAO.getConnectionInstance(getApplicationContext());


                    try {
                        tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_C);

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());
                        tabelaServicoDAO.editar(ts);

                        usuarios = SocketManagement.getDataTCP(LoginActivity.getUsuario(), ip, porta);

                        Log.i("UPE", "Editou!");

                    } catch (ServicoInexistenteException e) {
                        tabelaServicoDAO.inserir(ts);
                        try {
                            usuarios = SocketManagement.getDataTCP(LoginActivity.getUsuario(), ip, porta);
                            return "sucess";
                        } catch (UsuarioInexistenteException e1) {
                            return "Error";
                        }
                    } catch (UsuarioInexistenteException e) {
                        return "Error";
                    }

                    Log.i("UPE", p + "");


                }catch(IOException e){
                    return "Error";
                }


                return "sucess";
            }

            @Override
            protected void onPostExecute(String s) {

                if(s.equals("Error")) {
                    Log.i("UPE", s);
                    Toast.makeText(getBaseContext(), "Sem conexão com o servidor. Tente novamente", Toast.LENGTH_SHORT).show();
                }

                if (s.equals("sucess")) {
                    createListView();
                }

            }
        };

        task.execute();

    }


    private void gerarLista (final TabelaServico tabelaServico) {

        count ++;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {


                try {
                    usuarios = SocketManagement.getDataTCP(LoginActivity.getUsuario(), tabelaServico.getIp(), tabelaServico.getPorta());

                } catch (IOException e) {
                    return "Error";
                } catch (UsuarioInexistenteException e) {
                    return "Error";
                }

                return "sucess";
            }

            @Override
            protected void onPostExecute(String s) {

                if (s.equals("Error")) {
                    if(count < 2) {
                        gerarLista(tabelaServico);
                    } else {
                        Log.i("UPE", "Mais de três vezes");
                        gerarLista();
                    }
                }

                if (s.equals("sucess")) {
                   createListView();
                }
            }
        };

        task.execute();
    }
}

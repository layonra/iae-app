package com.iae.controller;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import com.iae.R;
import com.iae.dao.TabelaServicoDAO;
import com.iae.entity.TabelaServico;
import com.iae.entity.Usuario;
import com.iae.util.ServicoInexistenteException;
import com.iae.util.SocketManagement;
import com.iae.util.URL;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);


        final EditText nome = (EditText) findViewById(R.id.nome_edt);
        final EditText telefone = (EditText) findViewById(R.id.telefone_edt);
        final EditText cidade = (EditText) findViewById(R.id.cidade_edt);
        final EditText estado = (EditText) findViewById(R.id.estado_edt);
        final Button cadastrar = (Button) findViewById(R.id.cadastrar_button);


        cadastrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nome.getText().length() <= 0) {
                    nome.setError("Preencha o campo!");
                    nome.requestFocus();

                } else if (telefone.getText().length() <= 0) {
                    telefone.setError("Preencha o campo!");
                    telefone.requestFocus();
                } else if (cidade.getText().length() <= 0) {
                    cidade.setError("Preencha o campo!");
                    cidade.requestFocus();
                } else if (estado.getText().length() <= 0) {
                    estado.setError("Preencha o campo!");
                    estado.requestFocus();
                } else {

                    Usuario usuario = new Usuario(nome.getText().toString(), telefone.getText().toString(),
                            cidade.getText().toString(), estado.getText().toString(), null, null, null);


                    try {
                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        TabelaServico ts = tabelaServicoDAO.buscar(URL.PROCESSO.PROCESS_A);

                        cadastrarUsuario(usuario, ts);

                    } catch (ServicoInexistenteException e) {
                        cadastrarUsuario(usuario);
                    }

                    }
                }
            }

            );
        }


    private void cadastrarUsuario(final Usuario usuario) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data = "";
                try {
                    data = SocketManagement.sendDataUDP(URL.PROCESSO.PROCESS_A, URL.IP.IP_DNS, URL.PORTA.PORTA_DNS);

                    Log.i("UPE", data);


                    String[] s = data.split(":");
                    String ip = s[0];
                    String p = s[1];
                    int porta = Integer.parseInt(p);

                    TabelaServico ts = new TabelaServico(URL.PROCESSO.PROCESS_A, ip, porta);

                    TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                    tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                    tabelaServicoDAO.inserir(ts);

                    Log.i("UPE", p + "");

                    SocketManagement.sendDataTCP(usuario, ip, porta, 1);

                } catch (IOException e) {
                    cadastrarUsuario(usuario);
                }

                return data;
            }

            @Override
            protected void onPostExecute(String s) {

                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        };

        task.execute();

    }

    private void cadastrarUsuario (final Usuario usuario, final TabelaServico tabelaServico) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data = "";
                try {

                    SocketManagement.sendDataTCP(usuario, tabelaServico.getIp(), tabelaServico.getPorta(), 1);

                } catch (IOException e) {
                    cadastrarUsuario(usuario, tabelaServico);
                }

                return data;
            }

            @Override
            protected void onPostExecute(String s) {

                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        };

        task.execute();

    }

}

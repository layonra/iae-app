package com.montandoagaragem.controller;


import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import com.montandoagaragem.R;
import com.montandoagaragem.dao.TabelaServicoDAO;
import com.montandoagaragem.entity.TabelaServico;
import com.montandoagaragem.entity.Usuario;
import com.montandoagaragem.util.ServicoInexistenteException;
import com.montandoagaragem.util.SocketManagement;
import com.montandoagaragem.util.TimeoutThread;
import com.montandoagaragem.util.URL;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Cadastrar Usuário");
        }


        final EditText nome = (EditText) findViewById(R.id.nome_edt);
        final EditText telefone = (EditText) findViewById(R.id.telefone_edt);
        final EditText email = (EditText) findViewById(R.id.email_edt);
        final EditText senha = (EditText) findViewById(R.id.senha_edt);
        final EditText instrumento = (EditText) findViewById(R.id.instrumento_edt);
        final EditText genero = (EditText) findViewById(R.id.genero_edt);
        final EditText subGenero = (EditText) findViewById(R.id.sub_genero_edt);
        final EditText cidade = (EditText) findViewById(R.id.cidade_edt);
        final EditText estado = (EditText) findViewById(R.id.estado_edt);

        Button cadastrar = (Button) findViewById(R.id.cadastrar_button);


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

                    Usuario usuario = new Usuario(nome.getText().toString(), telefone.getText().toString(), email.getText().toString(),
                            senha.getText().toString(), instrumento.getText().toString(), genero.getText().toString(), subGenero.getText().toString(),
                            cidade.getText().toString(), estado.getText().toString());

                    count = 0;

                    try {
                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        TabelaServico ts = tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_A);

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

                String data;
                try {
                    TimeoutThread timeoutThread = new TimeoutThread();

                    Thread t = new Thread(timeoutThread);

                    t.start();
                    data = SocketManagement.sendDataUDP(URL.PROCESSO.PROCESSO_A, URL.IP.IP_DNS, URL.PORTA.PORTA_DNS);

                    Log.i("UPE", data);

                        String[] s = data.split(":");
                        String ip = s[0];
                        String p = s[1];
                        int porta = Integer.parseInt(p);

                        TabelaServico ts = new TabelaServico(URL.PROCESSO.PROCESSO_A, ip, porta);

                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        try {
                            tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_A);

                            tabelaServicoDAO.getConnectionInstance(getApplicationContext());
                            tabelaServicoDAO.editar(ts);
                            SocketManagement.sendDataTCP(usuario, ip, porta);
                            Log.i("UPE", "Editou!");

                        } catch (ServicoInexistenteException e) {
                            tabelaServicoDAO.inserir(ts);
                            SocketManagement.sendDataTCP(usuario, ip, porta);
                        }

                        Log.i("UPE", p + "");


                    }catch(IOException e){
                            return "Error";
                    }


                return data;
            }

            @Override
            protected void onPostExecute(String s) {

                if(s.equals("Error")) {
                    Log.i("UPE", s);
                    Toast.makeText(getBaseContext(), "Sem conexão com o servidor. Tente novamente", Toast.LENGTH_SHORT).show();
                }

            }
        };

        task.execute();

    }

    private void cadastrarUsuario (final Usuario usuario, final TabelaServico tabelaServico) {

        count ++;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data = "";
                try {

                    SocketManagement.sendDataTCP(usuario, tabelaServico.getIp(), tabelaServico.getPorta());

                } catch (IOException e) {
                    return "Error";
                }

                return data;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s.equals("Error")) {
                    if(count < 2) {
                        cadastrarUsuario(usuario, tabelaServico);
                    } else {
                        Log.i("UPE", "Mais de três vezes");
                        cadastrarUsuario(usuario);
                    }
                } else {
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute();

    }

}

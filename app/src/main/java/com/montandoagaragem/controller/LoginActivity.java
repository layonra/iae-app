package com.montandoagaragem.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.montandoagaragem.R;
import com.montandoagaragem.dao.TabelaServicoDAO;
import com.montandoagaragem.entity.TabelaServico;
import com.montandoagaragem.util.ServicoInexistenteException;
import com.montandoagaragem.util.SocketManagement;
import com.montandoagaragem.util.TimeoutThread;
import com.montandoagaragem.util.URL;
import com.montandoagaragem.util.UsuarioInexistenteException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText email = (EditText) findViewById(R.id.login_edt);
        final EditText senha = (EditText) findViewById(R.id.senha_edt);
        Button cadastroBtn = (Button) findViewById(R.id.cadastrar_button);
        Button logarBtn = (Button) findViewById(R.id.logar_button);

        cadastroBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
                startActivity(it);
            }
        });

        logarBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().length() <= 0) {
                    email.setError("Preencha o campo!");
                    email.requestFocus();
                } else if (senha.getText().toString().length() <= 0) {
                    senha.setError("Preencha o campo!");
                    senha.requestFocus();
                } else {

                    count = 0;

                    try {
                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        TabelaServico ts = tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_B);
                        logar(email.getText().toString(), senha.getText().toString(), ts);

                    } catch (ServicoInexistenteException e) {
                        logar(email.getText().toString(), senha.getText().toString());
                    }
                }
            }
        });
    }


    private void logar(final String email, final String senha) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data;

                    try {

                        TimeoutThread timeoutThread = new TimeoutThread();

                        Thread t = new Thread(timeoutThread);

                        t.start();
                        data = SocketManagement.sendDataUDP(URL.PROCESSO.PROCESSO_B, URL.IP.IP_DNS, URL.PORTA.PORTA_DNS);

                        Log.i("UPE", data);


                        String[] s = data.split(":");
                        String ip = s[0];
                        String p = s[1];
                        int porta = Integer.parseInt(p);

                        TabelaServico ts = new TabelaServico(URL.PROCESSO.PROCESSO_B, ip, porta);

                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        try {
                            tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_B);

                            tabelaServicoDAO.getConnectionInstance(getApplicationContext());
                            tabelaServicoDAO.editar(ts);
                            SocketManagement.sendDataTCP(email + ";" + senha, ts.getIp(), ts.getPorta());
                            Log.i("UPE", "Editou!");

                        }catch (ServicoInexistenteException e) {

                            tabelaServicoDAO.inserir(ts);

                            try {
                                SocketManagement.sendDataTCP(email + ";" + senha, ts.getIp(), ts.getPorta());

                            }catch (UsuarioInexistenteException u) {
                                return "Usuario inexistente";
                            }
                        } catch (UsuarioInexistenteException e) {
                            return "Usuario Inexistente";
                        }

                        Log.i("UPE", p + "");

                    } catch (IOException e) {
                            return "Error";

                    }

                return data;
            }

            @Override
            protected void onPostExecute(String s) {

                if(s.equals("Error")) {
                    Toast.makeText(getBaseContext(), "Sem conexão com o servidor. Tente novamente", Toast.LENGTH_SHORT).show();
                } else if(s.equals("Usuario Inexistente")) {
                    Toast.makeText(getBaseContext(), "Usuário inexistente no sistema!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute();

    }


    private void logar (final String email, final String senha, final TabelaServico tabelaServico) {

        count ++;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data = "";
                try {

                    SocketManagement.sendDataTCP(email + ";" + senha, tabelaServico.getIp(), tabelaServico.getPorta());

                } catch (IOException e) {
                    return "Error";
                } catch (UsuarioInexistenteException e) {
                    return "Usuario Inexistente";
                }

                return data;
            }
            @Override
            protected void onPostExecute(String s) {

                if (s.equals("Error")) {
                    if(count < 2) {
                        logar(email, senha, tabelaServico);
                    } else {
                        Log.i("UPE", "Mais de três vezes");
                        logar(email, senha);
                    }
                } else if(s.equals("Usuario Inexistente")) {
                    Toast.makeText(getBaseContext(), "Usuário inexistente no sistema!", Toast.LENGTH_SHORT).show();
                }

            }
        };

        task.execute();

    }
}

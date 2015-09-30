package com.montandoagaragem.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    private static int count = 0;

    private static Usuario usuario;

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
                        //Instancia um objeto da tabela de serviços que carrega os dados da conexão (IP, porta, Serviço)
                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        //Pega o contexto da aplicação
                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        //Busca a existência do processo no banco
                        TabelaServico ts = tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_B);

                        //Conexão direta com o servidor
                        logar(email.getText().toString(), senha.getText().toString(), ts);

                    } catch (ServicoInexistenteException e) {
                        //Conexão consultando o DNS
                        logar(email.getText().toString(), senha.getText().toString());
                    }
                }
            }
        });
    }

    /**
     * Método de consulta ao servidor de nomes
     * @param email email do usuario
     * @param senha senha
     */


    private void logar(final String email, final String senha) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data;

                    try {

                        //Thread de timeout encerra a conexão quando passa até 5 segundos sem resposta
                        TimeoutThread timeoutThread = new TimeoutThread();

                        Thread t = new Thread(timeoutThread);

                        t.start();

                        //Consulta quem possuí o processo no servidor de nomes
                        data = SocketManagement.sendDataUDP(URL.PROCESSO.PROCESSO_B, URL.IP.IP_DNS, URL.PORTA.PORTA_DNS);

                        String[] s = data.split(":");
                        String ip = s[0];
                        String p = s[1];
                        int porta = Integer.parseInt(p);

                        TabelaServico ts = new TabelaServico(URL.PROCESSO.PROCESSO_B, ip, porta);

                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        Usuario u = new Usuario();

                        u.setEmail(email);
                        u.setSenha(senha);
                        try {
                            //Busca processo no banco
                            tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_B);

                            tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                            //Se já existir atualiza
                            tabelaServicoDAO.editar(ts);
                            //Conexão com o servidor
                            usuario = SocketManagement.sendDataTCP(u, ts.getIp(), ts.getPorta());

                        }catch (ServicoInexistenteException e) {
                            //Se o processo não existir, insere e faz a conexão com o servidor
                            tabelaServicoDAO.inserir(ts);

                            try {
                              usuario = SocketManagement.sendDataTCP(u, ts.getIp(), ts.getPorta());
                                return "sucess";
                            }catch (UsuarioInexistenteException err) {
                                //Captura as exeções e retorna para o usario como mensagens de erro
                                return "Usuario inexistente";
                            }
                        } catch (UsuarioInexistenteException e) {
                            return "Usuario Inexistente";
                        }

                    } catch (IOException e) {
                            return "Error";

                    }

                return "sucess";
            }

            @Override
            protected void onPostExecute(String s) {

                if(s.equals("Error")) {
                    Toast.makeText(getBaseContext(), "Sem conexão com o servidor. Tente novamente", Toast.LENGTH_SHORT).show();
                } else if(s.equals("Usuario Inexistente")) {
                    Toast.makeText(getBaseContext(), "Usuário inexistente no sistema!", Toast.LENGTH_SHORT).show();
                }

                if (s.equals("sucess")) {
                    Intent it = new Intent(LoginActivity.this, ConsultaActivity.class);
                    startActivity(it);
                }
            }
        };

        task.execute();

    }


    /**
     * Método não consulta o servidor de nomes
     * @param email email
     * @param senha senha
     * @param tabelaServico tabela de armazemanto dos dados do serviço: IP, porta;
     */
    private void logar (final String email, final String senha, final TabelaServico tabelaServico) {

        count ++;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

               String data = "err";
                try {

                    Usuario u = new Usuario();
                    u.setEmail(email);
                    u.setSenha(senha);
                    usuario = SocketManagement.sendDataTCP(u,tabelaServico.getIp(), tabelaServico.getPorta());

                    if (usuario != null) {
                        data = "sucess";
                    }

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
                        logar(email, senha);
                    }
                } else if(s.equals("Usuario Inexistente")) {
                    Toast.makeText(getBaseContext(), "Usuário inexistente no sistema!", Toast.LENGTH_SHORT).show();
                } else if(s.equals("err")) {
                    Toast.makeText(getBaseContext(), "Usuário inválido", Toast.LENGTH_SHORT).show();
                }

                if (s.equals("sucess")) {
                    Intent it = new Intent(LoginActivity.this, ConsultaActivity.class);
                    startActivity(it);
                }

            }
        };

        task.execute();

    }


    public static Usuario getUsuario() {
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario;
    }

    public static void setUsuario(Usuario u) {
        usuario = u;
    }
}

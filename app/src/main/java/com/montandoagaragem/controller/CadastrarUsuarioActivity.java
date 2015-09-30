package com.montandoagaragem.controller;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.montandoagaragem.util.UsuarioInexistenteException;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);


       //Seta o Toobar com o nome da aplicação
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
        final EditText bairro = (EditText) findViewById(R.id.estado_edt);

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
                } else if (email.getText().length() <= 0) {
                    email.setError("Preencha o campo!");
                    email.requestFocus();
                } else if (senha.getText().length() <= 0) {
                    senha.setError("Preencha o campo!");
                    senha.requestFocus();
                } else if (instrumento.getText().length() <= 0) {
                    instrumento.setError("Preencha o campo!");
                    instrumento.requestFocus();
                } else if (genero.getText().length() <= 0) {
                    genero.setError("Preencha o campo!");
                    genero.requestFocus();
                } else if (subGenero.getText().length() <= 0) {
                    subGenero.setError("Preencha o campo!");
                    subGenero.requestFocus();
                } else if (cidade.getText().length() <= 0) {
                    cidade.setError("Preencha o campo!");
                    cidade.requestFocus();
                } else if (bairro.getText().length() <= 0) {
                    bairro.setError("Preencha o campo!");
                    bairro.requestFocus();
                } else {

                    Usuario usuario = new Usuario(nome.getText().toString(), telefone.getText().toString(), email.getText().toString(),
                            senha.getText().toString(), instrumento.getText().toString(), genero.getText().toString(), subGenero.getText().toString(),
                            cidade.getText().toString(), bairro.getText().toString());

                    count = 0;

                    try {
                        //Instancia um objeto da tabela de serviços que carrega os dados da conexão (IP, porta, Serviço)
                        TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                        //Pega o contexto
                        tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                        //Busca a existência do processo no banco
                        TabelaServico ts = tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_A);

                        //Conexão direta com o servidor
                        cadastrarUsuario(usuario, ts);

                    } catch (ServicoInexistenteException e) {
                        //Conexão consultando o DNS
                        cadastrarUsuario(usuario);
                    }

                    }
                }
            }

            );
        }

    /**
     * Método de consulta ao servidor de nomes
     * @param usuario
     */

    private void cadastrarUsuario(final Usuario usuario) {

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
                    data = SocketManagement.sendDataUDP(URL.PROCESSO.PROCESSO_A, URL.IP.IP_DNS, URL.PORTA.PORTA_DNS);


                    //Divide a String em IP e porta
                    String[] s = data.split(":");
                    String ip = s[0];
                    String p = s[1];
                    int porta = Integer.parseInt(p);


                    TabelaServico ts = new TabelaServico(URL.PROCESSO.PROCESSO_A, ip, porta);

                    TabelaServicoDAO tabelaServicoDAO = new TabelaServicoDAO();

                    tabelaServicoDAO.getConnectionInstance(getApplicationContext());

                    LoginActivity.getUsuario();

                    Usuario u;

                        try {
                            //Busca processo no banco
                            tabelaServicoDAO.buscar(URL.PROCESSO.PROCESSO_A);

                            //Se já existir atualiza
                            tabelaServicoDAO.getConnectionInstance(getApplicationContext());
                            tabelaServicoDAO.editar(ts);

                           //Conexão TCP com o servidor. Para inserir um novo usuário
                            u = SocketManagement.sendDataTCP(usuario, ip, porta);
                            //Atualiza o objeto de login
                            LoginActivity.setUsuario(u);

                        } catch (ServicoInexistenteException e) {
                            //Se o processo não existir, insere e faz a conexão com o servidor
                            tabelaServicoDAO.inserir(ts);
                            try {

                                u = SocketManagement.sendDataTCP(usuario, ip, porta);
                                LoginActivity.setUsuario(u);
                                //Retorna evento bem sucedido
                                return "sucess";
                            } catch (UsuarioInexistenteException e1) {
                                //Captura a exeção e passa como mensagem de erro ao usuário
                                return "Error";
                            }
                        } catch (UsuarioInexistenteException e) {

                           return "Error";
                        }

                    }catch(IOException e){
                            return "Error";
                    }


                return "sucess";
            }

            @Override
            protected void onPostExecute(String s) {

                if(s.equals("Error")) {
                    Toast.makeText(getBaseContext(), "Sem conexão com o servidor. Tente novamente", Toast.LENGTH_SHORT).show();
                }

                if (s.equals("sucess")) {
                    //Caso bem sucedido. Chama a Activity de consulta
                    Intent it = new Intent(CadastrarUsuarioActivity.this, ConsultaActivity.class);
                    startActivity(it);
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        };

        task.execute();

    }

    /**
     * Método que não consulta o servidor de nomes
     * @param usuario
     * @param tabelaServico
     */
    private void cadastrarUsuario (final Usuario usuario, final TabelaServico tabelaServico) {

        //Contador para o número de tentativas de uma conexão
        count ++;

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {


                try {

                    LoginActivity.getUsuario();
                    //Conexão TCP direta com o servidor
                    Usuario u = SocketManagement.sendDataTCP(usuario, tabelaServico.getIp(), tabelaServico.getPorta());

                    LoginActivity.setUsuario(u);

                } catch (IOException e) {
                    return "Error";
                } catch (UsuarioInexistenteException e) {
                    return "Error";
                }

                return "sucess";
            }

            @Override
            protected void onPostExecute(String s) {

                //Caso seja mal sucedido. O metodo é chamado novamente antes de se chamar o servidor de nomes

                if (s.equals("Error")) {
                    if(count < 2) {
                        cadastrarUsuario(usuario, tabelaServico);
                    } else {
                        cadastrarUsuario(usuario);
                    }
                }

                if (s.equals("sucess")) {
                    //Caso bem sucedido. Chama a Activity de consulta
                    Intent it = new Intent(CadastrarUsuarioActivity.this, ConsultaActivity.class);
                    startActivity(it);
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };

        task.execute();

    }

}

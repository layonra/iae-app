package lab.emerson.iae.controller;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import lab.emerson.iae.R;
import lab.emerson.iae.entity.Usuario;
import lab.emerson.iae.util.SocketManagement;
import lab.emerson.iae.util.URL;
import lab.emerson.iae.util.CreateProtocol;
import server.ClientServer;

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

                    //String message = CreateProtocol.generateProtocol(URL.PROCESSO.PROCESS_A, usuario);

                    //receiverMessage();
                    cadastrarUsuario(usuario.toString());
                }
            }
        });
    }


    public void cadastrarUsuario(final String message) {
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

                    Log.i("UPE", p + "");

                    SocketManagement.sendDataTCP(message, ip, porta);

                } catch (IOException e) {

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


    public void receiverMessage() {


        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                 return ClientServer.receiverMessage();
            }

            @Override
            protected void onPostExecute(String s) {

                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

}

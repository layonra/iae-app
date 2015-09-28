package com.iae.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iae.R;
import com.iae.util.SocketManagement;
import com.iae.util.URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

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
                String em = "@teste";
                String pass = "1234";

                logar(em, pass);
            }
        });
    }

    public void logar(final String email, final String senha) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String data = "";
                try {

                    List<String> message = new ArrayList<>();

                    message.add(email);
                    message.add(senha);

                    SocketManagement.sendDataTCP(message, URL.IP.ENDERECO_SERVIDOR_A, URL.PORTA.PORTA_SERVIDOR_A, 2);

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
}

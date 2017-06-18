package com.example.root.poalabmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editLogin, editSenha;
    private Context context;
    private UsersController usuarioController;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        try {
            usuarioController = UsersController.getInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editLogin = (EditText) findViewById(R.id.email);
        editSenha = (EditText) findViewById(R.id.password);
        try {
            testaInicializacao();
        } catch (Exception e) {
            exibeDialogo("Erro inicializando banco de dados");
            //exibeDialogo(e.getMessage());
            e.printStackTrace();
        }

        Button login_button = (Button) findViewById(R.id.email_sign_in_button);//seta o botao de entrar
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validar(v);
            }
        });

        Button new_user_button = (Button) findViewById(R.id.new_user_button);//seta o botao de novo usuario
        new_user_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NewUsersActivity.class);
                startActivity(intent);
            }
        });
    }

    public void testaInicializacao() throws Exception {
        if (usuarioController.findAll().isEmpty()){
            Users usuario = new Users(null, "admin", "admin");
            usuarioController.insert(usuario);
        }
    }

    public void exibeDialogo(String mensagem) {
        alert = new AlertDialog.Builder(context);
        alert.setPositiveButton("OK", null);
        alert.setMessage(mensagem);
        alert.create().show();
    }

    public void validar(View view) {
        String login = editLogin.getText().toString();
        String senha = editSenha.getText().toString();
        try {
            boolean isValid = usuarioController.validaLogin(login, senha);
            if (isValid) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            } else {
                exibeDialogo("Verifique login e senha!");
            }
        } catch (Exception e) {
            exibeDialogo("Erro validando login e senha");
            e.printStackTrace();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }*/



}


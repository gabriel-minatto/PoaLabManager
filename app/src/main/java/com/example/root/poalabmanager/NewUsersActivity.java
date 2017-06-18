package com.example.root.poalabmanager;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.root.poalabmanager.controllers.UsersController;
import com.example.root.poalabmanager.models.Users;

public class NewUsersActivity extends AppCompatActivity {

    private EditText editLogin, editSenha;
    private Context context;
    private UsersController usuarioController;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_users);
        context = this;
        try {
            usuarioController = UsersController.getInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editLogin = (EditText) findViewById(R.id.new_user_login);
        editSenha = (EditText) findViewById(R.id.new_user_password);

        Button new_user_button = (Button) findViewById(R.id.new_user_form_button);//seta o botao de novo usuario
        new_user_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewUser();
            }
        });

        Button clean_users_button = (Button) findViewById(R.id.clean_users_button);//seta o botao de novo usuario
        clean_users_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteAllUsers();
            }
        });
    }

    private void createNewUser(){
        String login = editLogin.getText().toString();
        String senha = editSenha.getText().toString();

        Users usuario =  new Users(login, senha);
        try {
            boolean i = usuarioController.insert(usuario);
            if(i){
                super.onBackPressed();
            }

            exibeDialogo("Algo deu errado!");

        } catch (Exception e) {
            exibeDialogo("Algo deu errado!");
            //exibeDialogo(e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteAllUsers(){
        try {
            usuarioController.deleteAll();
            super.onBackPressed();
        } catch (Exception e) {
            exibeDialogo("Algo deu errado!");
            //exibeDialogo(e.getMessage());
            e.printStackTrace();
        }
    }

    public void exibeDialogo(String mensagem) {
        alert = new AlertDialog.Builder(context);
        alert.setPositiveButton("OK", null);
        alert.setMessage(mensagem);
        alert.create().show();
    }
}

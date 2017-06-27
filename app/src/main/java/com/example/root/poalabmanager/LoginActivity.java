package com.example.root.poalabmanager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.poalabmanager.controllers.UsersController;
import com.example.root.poalabmanager.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * A login screen that offers login via email/password.
 */
@RuntimePermissions
public class LoginActivity extends AppCompatActivity {

    private EditText editLogin, editSenha;
    private Context context;
    private UsersController usuarioController;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        LoginActivityPermissionsDispatcher.checkPermissionsWithCheck(LoginActivity.this);

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

        Button firebaseLogin = (Button)findViewById(R.id.firebase_login_btn);
        firebaseLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FirebaseLoginActivity.class);
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
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Users user = usuarioController.findUserByLogin(login);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
            } else {
                exibeDialogo("Verifique login e senha!");
            }
        } catch (Exception e) {
            exibeDialogo("Erro validando login e senha");
            e.printStackTrace();
        }
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void checkPermissions(){
        Toast.makeText(this,"Testando permissões necessárias...",Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {

        showRationaleDialog(request,"Câmera");
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForStorage(PermissionRequest request) {

        showRationaleDialog(request,"Memória");
    }

    @OnShowRationale(Manifest.permission.INTERNET)
    void showRationaleForInternet(PermissionRequest request) {

        showRationaleDialog(request,"Internet");
    }

    private void showRationaleDialog(final PermissionRequest request, String recurso) {
        new AlertDialog.Builder(this)
                .setMessage("Permitir acesso a "+recurso)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request.cancel();
                    }
                })
                .show();
    }
}


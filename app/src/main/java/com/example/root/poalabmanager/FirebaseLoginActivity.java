package com.example.root.poalabmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FirebaseLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private UsersController usuarioController;
    private String login;
    private String senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);
        loadFireBaseAuth();
        loadUsersController();

        Button firebaseLogin = (Button)findViewById(R.id.firebase_auth_btn);
        firebaseLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reloadLoginPassword();
                validarFirebase(login,senha);
            }
        });
    }

    public void validarFirebase(String login, String senha){
        this.mAuth.signInWithEmailAndPassword(login,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(FirebaseLoginActivity.this,"Falha ao efetuar o Login: "+ task.getException(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(FirebaseLoginActivity.this,"Login Efetuado com sucesso!!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FirebaseLoginActivity.this, MainActivity.class);
                    Users user = null;
                    try {
                        user = loadOrCreateUser(FirebaseLoginActivity.this.login,FirebaseLoginActivity.this.senha);
                        if(user == null)
                            throw new Exception();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("user",(Serializable) user);
                    startActivity(intent);
                }
            }
        });
    }

    private void reloadLoginPassword(){
        this.login = ((EditText)findViewById(R.id.firebase_email)).getText().toString();
        this.senha = ((EditText)findViewById(R.id.firebase_password)).getText().toString();
    }

    private Users loadOrCreateUser(String login,String senha) throws Exception {
        Users user = this.usuarioController.userExists(login);
        if(user == null){
            this.usuarioController.insert(new Users(login,senha));
            user = this.usuarioController.userExists(login);
        }
        return user;
    }

    private void loadUsersController(){
        try {
            this.usuarioController = UsersController.getInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFireBaseAuth(){
        this.mAuth = FirebaseAuth.getInstance();

        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("AUTH", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("AUTH", "onAuthStateChanged:signed_out");
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

package com.example.root.poalabmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.poalabmanager.controllers.ProjectsController;
import com.example.root.poalabmanager.models.Projects;
import com.example.root.poalabmanager.models.Users;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private Users user;
    private Context context;
    private ProjectsController projectController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        this.user = (Users) getIntent().getExtras().getSerializable("user");
        try {
            this.projectController = ProjectsController.getInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.projectController.findByUser(this.user.getId()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add_project_fab = (FloatingActionButton) findViewById(R.id.add_project);
        add_project_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Novo Projeto");

                // Set up the input
                final EditText novo_projeto = new EditText(view.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                novo_projeto.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(novo_projeto);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Projects project = new Projects(novo_projeto.getText().toString(), MainActivity.this.user.getId());
                        try {
                            MainActivity.this.projectController.insert(project);
                            showToastMessage("Projeto " + project.getName() + " inserido com sucesso");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        //setTitle("Olá "+getIntent().getExtras().getString("user_name"));
        //Users user = (Users) getIntent().getExtras().getSerializable("user");
    }

    public void showToastMessage(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}

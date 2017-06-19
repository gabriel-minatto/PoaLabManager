package com.example.root.poalabmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.poalabmanager.adapters.ProjectsAdapter;
import com.example.root.poalabmanager.controllers.ProjectsController;
import com.example.root.poalabmanager.models.Projects;
import com.example.root.poalabmanager.models.Users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Users user;
    private final Context context = this;
    public ProjectsController projectController;
    private List<Projects> projectsList = new ArrayList<>();
    private Toolbar toolbar;
    private FloatingActionButton add_project_fab;
    private RecyclerView projectsRecycler;
    private RecyclerView.LayoutManager projectsRecyclerManager;
    private ProjectsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.user = (Users) getIntent().getExtras().getSerializable("user");
        this.loadProjectsController();
        this.loadProjects();
        this.loadToolbar();
        this.loadNewProjectFab();

        this.loadProcetsListRecycler();

        //setTitle("Olá "+getIntent().getExtras().getString("user_name"));
        //Users user = (Users) getIntent().getExtras().getSerializable("user");
    }

    public void loadProcetsListRecycler(){
        this.projectsRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        this.projectsRecycler.setLayoutManager(new LinearLayoutManager(this));

        this.adapter = new ProjectsAdapter(this, this.projectsList);
        this.projectsRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        this.projectsRecycler.setAdapter(this.adapter);
    }

    public void loadNewProjectFab(){

        this.add_project_fab = (FloatingActionButton) findViewById(R.id.add_project);
        this.add_project_fab.setOnClickListener(new View.OnClickListener() {
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
                            MainActivity.this.projectsList.add(project);
                            MainActivity.this.adapter.notifyDataSetChanged();
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

    }

    public void loadToolbar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
    }

    public void loadProjects(){
        try {
            this.projectsList = this.projectController.findByUser(this.user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadProjectsController(){
        try {
            this.projectController = ProjectsController.getInstance(this.context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToastMessage(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}

package com.example.root.poalabmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.root.poalabmanager.models.Comments;
import com.example.root.poalabmanager.models.ImageModel;
import com.example.root.poalabmanager.models.Projects;
import com.example.root.poalabmanager.models.Users;
import com.example.root.poalabmanager.utils.SlugifyUtil;
import com.example.root.poalabmanager.RecyclerGalleryItemClickListener;
import com.example.root.poalabmanager.GalleryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity

    implements NavigationView.OnNavigationItemSelectedListener {
    private final int RESULT_LOAD_IMG = 2;
    private final int IMAGE_VIEW_ACTIVITY_REQUEST_CODE = 3;
    private Projects project;
    private Users user;

    private DatabaseReference dbRef;
    private DatabaseReference comentsRef;

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    ArrayList<ImageModel> data = new ArrayList<>();


    public void loadProjectImages(){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PoaLabManager/"
                + project.getName() + project.getId() + "/Images/";
        File dir = new File(dirPath);
        if(dir.isDirectory()){
            File[] dirList = dir.listFiles();
            if(dirList != null){
                for (File child : dirList){
                    ImageModel image = new ImageModel();
                    image.setName(child.getName());
                    image.setUrl(child.getAbsolutePath());
                    data.add(image);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.project = (Projects)getIntent().getExtras().getSerializable("project");

        this.user = (Users)getIntent().getExtras().getSerializable("user");

        setTitle(this.project.getName());

        this.loadDbRefs();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //images
        loadProjectImages();
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new GalleryAdapter(MenuActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);

        //código para abrir os detalhes da imagem
        /*mRecyclerView.addOnItemTouchListener(new RecyclerGalleryItemClickListener(this,
                new RecyclerGalleryItemClickListener.OnGalleryItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(MenuActivity.this, DetailActivity.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));
        */
        FloatingActionButton new_comment_fab = (FloatingActionButton) findViewById(R.id.new_comment_fab);
        new_comment_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Novo comentário");

                // Set up the input
                final EditText novo_coment = new EditText(view.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                novo_coment.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(novo_coment);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Comments coment = new Comments(MenuActivity.this.project.getUser(),MenuActivity.this.project.getId());
                        coment.setText(novo_coment.getText().toString());
                        MenuActivity.this.updateChildRef();
                        MenuActivity.this.comentsRef.setValue(coment);
                        Toast.makeText(MenuActivity.this,"Enviado!",Toast.LENGTH_LONG).show();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void loadDbRefs(){
        String projectFolder =  SlugifyUtil.makeSlug(this.user.getLogin())+"_"+this.project.getHash()+"/";
        this.dbRef = FirebaseDatabase.getInstance().getReference("testes/coments/"+projectFolder);
        this.updateChildRef();
    }

    public void updateChildRef(){
        this.comentsRef = this.dbRef.child(getRandomComentName());
    }

    public String getRandomComentName(){
        SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return File.separator + m_sdf.format(new Date());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            //this.uploadProgress.setVisibility(View.VISIBLE);
            Intent camera = new Intent(this, CameraActivity.class);
            camera.putExtra("project",(Serializable) this.project);
            camera.putExtra("userLogin", this.user.getLogin());
            startActivityForResult(camera,this.IMAGE_VIEW_ACTIVITY_REQUEST_CODE);

        }

        if (id == R.id.nav_user_desc){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Informações do Usuário");

            builder.setMessage("ID: "+this.user.getId()+"\n"+"Login: "+this.user.getLogin());
            // Set up the buttons
            builder.setPositiveButton("OK", null);

            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume(){
        super.onResume();
        this.data.clear();
        this.loadProjectImages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == this.IMAGE_VIEW_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            this.data.clear();
            this.loadProjectImages();
        }
    }

}

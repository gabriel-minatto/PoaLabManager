package com.example.root.poalabmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.root.poalabmanager.models.Projects;
import com.example.root.poalabmanager.utils.SlugifyUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.root.poalabmanager.utils.FileUtil;

import id.zelory.compressor.Compressor;


public class CameraActivity extends AppCompatActivity {
    private final int PIC_CAPTURED = 1;
    private Projects project;
    private String userLogin;
    private FirebaseStorage firebase;
    private StorageReference storageRef;
    private StorageReference imageRef;
    private File pictureFile;
    private UploadTask upTask;
    private String firebasePath = "gs://poalabmanager.appspot.com/projetosteste/";
    private String childPath;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        this.project = (Projects) getIntent().getExtras().getSerializable("project");

        this.userLogin = getIntent().getExtras().getString("userLogin");

        this.progressDialog = new ProgressDialog(this);

        setTitle(this.project.getName());

        this.loadFirebaseStorages();

        // Handle the camera action
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,PIC_CAPTURED);
    }

    private void loadFirebaseStorages(){
        this.childPath = getProjectFolder()+getRandomImageName();

        this.firebase = FirebaseStorage.getInstance();
        this.storageRef = firebase.getReferenceFromUrl(this.firebasePath);
        this.imageRef = storageRef.child(this.childPath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PIC_CAPTURED && resultCode == RESULT_OK)
        {
            /*this.pictureView.setImageBitmap(BitmapFactory.decodeFile(this.pictureFile.getAbsolutePath()));
            this.pictureSizeTextView.setText(String.format("Size : %s", getReadableFileSize(this.pictureFile.length())));*/
            try {
                this.showProgressDialog("Enviando sua imagem...");
                this.compressImage(data);
                this.saveImageLocally();
                this.uploadImageToFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void compressImage(Intent data) throws IOException {
        this.pictureFile = FileUtil.from(this, data.getData());
        this.pictureFile = new Compressor(CameraActivity.this)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToFile(this.pictureFile);
    }

    private void saveImageLocally() throws IOException {
        InputStream is = null;
        OutputStream os = null;
        String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PoaLabManager/"
               + project.getName() + project.getId() + "/Images/";
        File destPathFile = new File(destPath);
        if(!destPathFile.exists()){
            destPathFile.mkdirs();
        }
        try {
            is = new FileInputStream(pictureFile.getAbsolutePath());
            os = new FileOutputStream(destPath + pictureFile.getName());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
        finally {
            is.close();
            os.close();
        }
    }

    private void uploadImageToFirebase(){

        this.upTask = this.imageRef.putFile(Uri.fromFile(this.pictureFile));

        upTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CameraActivity.this.showMessage("Ocorreu um erro durante o envio");
                CameraActivity.this.progressDialog.hide();
                CameraActivity.this.finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                CameraActivity.this.showMessage("Imagem enviada com sucesso");
                CameraActivity.this.progressDialog.hide();
                CameraActivity.this.finish();
            }
        });
    }

    private String getProjectFolder(){
        String loginSlug = SlugifyUtil.makeSlug(this.userLogin);
        return loginSlug+"_"+this.project.getHash()+"/";
    }

    private String getRandomImageName(){
        SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return File.separator + m_sdf.format(new Date()) + ".jpg";
    }


    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String msg){
        this.progressDialog.setMessage(msg);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setInverseBackgroundForced(false);
        this.progressDialog.show();
    }


}

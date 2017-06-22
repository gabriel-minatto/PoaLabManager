package com.example.root.poalabmanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.root.poalabmanager.models.Projects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends AppCompatActivity {
    private final int PIC_CAPTURED = 1;
    private Projects project;
    String filePath = Environment.getExternalStorageDirectory() + "/PoaLabManager";
    FirebaseStorage firebase;
    StorageReference storageRef;
    StorageReference imageRef;
    Bitmap pictureBitmap;
    UploadTask upTask;

    //String m_imagePath = Environment.getExternalStorageDirectory()+ File.separator + "PoaLabManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        this.project = (Projects) getIntent().getExtras().getSerializable("project");

        this.loadFirebaseStorages();

        // Handle the camera action
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,PIC_CAPTURED);
    }

    private void loadFirebaseStorages(){
        this.firebase = FirebaseStorage.getInstance();
        this.storageRef = firebase.getReferenceFromUrl("gs://poalabmanager.appspot.com/projetos/");
        this.imageRef = storageRef.child(this.project.getName()+"/"+getImageName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PIC_CAPTURED && resultCode == RESULT_OK)
        {
            this.pictureBitmap = (Bitmap) data.getExtras().get("data");

            this.uploadImageToFirebase();
        }
    }

    private byte[] getByteArrayFromImage(){
        ByteArrayOutputStream imageOutStream = new ByteArrayOutputStream();
        this.pictureBitmap.compress(Bitmap.CompressFormat.PNG,0,imageOutStream);
        return imageOutStream.toByteArray();
    }

    /*private byte[] getByteArrayFromImage(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.pictureBitmap.getByteCount());
        this.pictureBitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }*/

    private void uploadImageToFirebase(){

        this.upTask = this.imageRef.putBytes(this.getByteArrayFromImage());

        upTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CameraActivity.this,"Ocorreu um erro durante o envio",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CameraActivity.this,"Imagem enviada com sucesso",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private File createDirectoryAndSaveImage(Bitmap img, String filename){

        File direct = new File(this.filePath);

        if(!direct.exists()){
            //File newDirect = new File("/sdcard/"+m_imageFolder);
            File newDirect = new File(this.filePath);
            newDirect.mkdirs();
        }
        //File file = new File(new File("/sdcard/"+m_imageFolder),filename);
        File file = new File(new File(this.filePath),filename);

        if(file.exists()){
            file.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();
            //AndroidBmpUtil bmpUtil = new AndroidBmpUtil().save(img,file);
        }catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }

    private String getImageName(){
        SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return File.separator + m_sdf.format(new Date()) + ".PNG";
    }
}

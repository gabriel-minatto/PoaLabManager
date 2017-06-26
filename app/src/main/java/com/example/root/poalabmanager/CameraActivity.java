package com.example.root.poalabmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.root.poalabmanager.models.Projects;
import com.example.root.poalabmanager.utils.SlugifyUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        this.project = (Projects) getIntent().getExtras().getSerializable("project");

        this.userLogin = getIntent().getExtras().getString("userLogin");

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
                this.compressImage(data);
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

    private void uploadImageToFirebase(){

        this.upTask = this.imageRef.putFile(Uri.fromFile(this.pictureFile));
        this.showMessage("Enviando sua imagem...");

        upTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CameraActivity.this.showMessage("Ocorreu um erro durante o envio");
                CameraActivity.this.finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                CameraActivity.this.showMessage("Imagem enviada com sucesso");
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

    /*private byte[] getByteArrayFromImage(){
        ByteArrayOutputStream imageOutStream = new ByteArrayOutputStream();
        this.pictureBitmap.compress(Bitmap.CompressFormat.PNG,0,imageOutStream);
        return imageOutStream.toByteArray();
    }*/

    /*private byte[] getByteArrayFromImage(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.pictureBitmap.getByteCount());
        this.pictureBitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }*/

    /*private byte[] getByteArrayFromFile() throws IOException {
        byte[] data = new byte[(int) this.pictureFile.length()];
        new FileInputStream(this.pictureFile).read(data);
        return data;
    }*/



    /*@NonNull
    private void createDirectoryAndSaveFile(){

        File direct = new File(this.filePath);

        if(!direct.exists()){
            //File newDirect = new File("/sdcard/"+m_imageFolder);
            //File newDirect = new File(this.filePath);
            direct.mkdirs();
        }
        //File file = new File(new File("/sdcard/"+m_imageFolder),filename);
        this.pictureFile = new File(this.filePath,this.getImageName());

        if(this.pictureFile.exists()){
            this.pictureFile.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(this.pictureFile);
            out.write(getByteArrayFromImage());
            out.flush();
            out.close();
            //AndroidBmpUtil bmpUtil = new AndroidBmpUtil().save(img,file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

}

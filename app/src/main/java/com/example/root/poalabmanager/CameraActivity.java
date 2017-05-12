package com.example.root.poalabmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;

public class CameraActivity extends AppCompatActivity {
    private final int PIC_CAPTURED = 1;
    /*private Bitmap m_bitmap;
    String m_curentDateandTime;
    String m_imagePath =*/
    String m_imageFolder = "/PoaLabManager";

    //String m_imagePath = Environment.getExternalStorageDirectory()+ File.separator + "PoaLabManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Handle the camera action
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,PIC_CAPTURED);

        //camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PIC_CAPTURED && resultCode == RESULT_OK)
        {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            this.createDirectoryAndSaveImage(img,getImageName());

            Toast.makeText(CameraActivity.this,"Foto Retirada",Toast.LENGTH_SHORT).show();
            /*m_bitmap = ImageHelper.scaleImage(m_imagePath, 200, 200);
            m_bitmap = ImageHelper.rotateImage(m_bitmap, true, m_rotate);
            m_ivCaptureImage.setImageBitmap(m_bitmap);


            folderExists();

            Uri uriSavedImage= null;

            try {
                uriSavedImage = this.getImageUri();

                String teste = uriSavedImage.getPath();
                //Toast.makeText(CameraActivity.this,teste,Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    private void createDirectoryAndSaveImage(Bitmap img, String filename){

        String filePath = Environment.getExternalStorageDirectory() + m_imageFolder;
        File direct = new File(filePath);

        if(!direct.exists()){
            //File newDirect = new File("/sdcard/"+m_imageFolder);
            File newDirect = new File(filePath);
            newDirect.mkdirs();
        }
        //File file = new File(new File("/sdcard/"+m_imageFolder),filename);
        File file = new File(new File(filePath),filename);

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
    }

    private String getImageName(){
        SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return File.separator + m_sdf.format(new Date()) + ".png";
    }

    /*private void folderExists(){
        File folder = new File(m_imagePath);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    private Uri getImageUri() throws Exception
    {
        Uri m_imgUri = null;
        File m_file;
        String nome;
        try
        {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            m_curentDateandTime = m_sdf.format(new Date());
            nome = File.separator + m_curentDateandTime + ".jpg";
            //m_file = new File(m_imagePath);
            m_file = new File(Environment.getExternalStorageDirectory()+ File.separator + "PoaLabManager/",nome);
            m_imgUri = Uri.fromFile(m_file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return m_imgUri;
    }*/
}

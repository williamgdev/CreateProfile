package com.mac.fireflies.wgt.createprofile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class Main2Activity extends AppCompatActivity {
    TextView txtName;
    ImageView photo, album;
    File pictureCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //txtName = ((TextView) findViewById(R.id.myLabel));
        //Intent i = getIntent();
        //txtName.setText(i.getStringExtra("textInput"));
        photo = ((ImageView) findViewById(R.id.photo));
        album = ((ImageView) findViewById(R.id.album));

    }

    public void openCamera(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(i, 0);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK){
            Bundle extra = data.getExtras();
            Bitmap image = (Bitmap) extra.get("data");
            photo.setImageBitmap(image);
            File dir = new File(Environment.getExternalStorageDirectory(), "album");
            dir.mkdirs();
            File file = new File(dir, "File.jpg");


            try {
                // Very simple code to copy a picture from the application's
                // resource into the external file.  Note that this code does
                // no error checking, and assumes the picture is small (does not
                // try to copy it in chunks).  Note that if external storage is
                // not currently mounted this will silently fail.
                OutputStream os = new FileOutputStream(file);
                byte[] datai = new byte[image.getRowBytes()];
                os.write(datai);
                os.close();
            } catch (Exception e) {
                // Unable to create file, likely because external storage is
                // not currently mounted.
                Log.w("ExternalStorage", "Error writing " + file, e);
            }
            File inputFile = new File(getExternalFilesDir(null), "File.jpg");
            try {

                InputStream os = new FileInputStream(file);
                byte[] datai = new byte[os.available()];
                for (int i=0; i< datai.length; i++){
                    datai[i] = Byte.parseByte(os.read() + "");
                }
                os.close();
                Intent i = getIntent();
                Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(null)+"/File.jpg");
                album.setImageBitmap(bitmap);

            } catch (Exception e) {
                // Unable to create file, likely because external storage is
                // not currently mounted.
                Log.w("ExternalStorage", "Error writing " + file, e);
            }
        }
    }
}

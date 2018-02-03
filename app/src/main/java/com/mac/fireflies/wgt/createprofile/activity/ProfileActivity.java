package com.mac.fireflies.wgt.createprofile.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.ProfilePresenter;
import com.mac.fireflies.wgt.createprofile.presenter.ProfilePresenterImpl;
import com.mac.fireflies.wgt.createprofile.view.ProfileView;

public class ProfileActivity extends AppCompatActivity implements ProfileView {
    EditText txtName;
    ImageView photo;
    Button bSave, bDelete;
    ProfilePresenter profilePresenter;
    private TextView txtEmail;
    static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePresenter = new ProfilePresenterImpl();
        profilePresenter.attachView(this);
        txtName = (EditText) findViewById(R.id.profile_name);
        photo = (ImageView) findViewById(R.id.profile_photo);
        txtEmail = (TextView) findViewById(R.id.profile_email);
        bSave = (Button) findViewById(R.id.profile_button_save);
        bDelete = (Button) findViewById(R.id.profile_button_delete);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePresenter.updateOrCreateProfile();
            }
        });
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePresenter.deleteProfile();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        profilePresenter.loadData(getIntent());
    }

    @Override
    public void showText(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setInfo(String email) {
        txtEmail.setText(email);
    }

    @Override
    public void setProfile(String name, String email) {
        if (name != null) {
            txtName.setText(name);
        }
        txtEmail.setText(email);
    }

    @Override
    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public String getName() {
        return txtName.getText().toString();
    }

    @Override
    public Bitmap getPhoto() {
        photo.setDrawingCacheEnabled(true);
        photo.buildDrawingCache();
        return photo.getDrawingCache();
    }

    @Override
    public void setPhoto(Uri uri) {
        Glide.with(this)
                .load(uri)
                .into(photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
            profilePresenter.savePhoto();
        }
    }
}

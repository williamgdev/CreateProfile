package com.mac.fireflies.wgt.createprofile.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.ProfilePresenter;
import com.mac.fireflies.wgt.createprofile.presenter.ProfilePresenterImpl;
import com.mac.fireflies.wgt.createprofile.view.ProfileView;

public class ProfileActivity extends AppCompatActivity implements ProfileView {
    EditText txtName;
    ImageView photo;
    Button bSave, bDelete, bUpload;
    private Bitmap currentBitmap;
    ProfilePresenter profilePresenter;
    private TextView txtEmail;
    static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int RESULT_PICK_PHOTO = 200;

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
        bUpload = (Button) findViewById(R.id.profile_button_upload);
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhotoFromLibrary();
            }
        });

        profilePresenter.loadData(getIntent());
    }

    @Override
    public void pickPhotoFromLibrary() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_PICK_PHOTO);
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
    public void showProgress(boolean b) {

    }

    @Override
    public void hideProgress() {

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
    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    @Override
    public void setPhoto(Uri uri) {
        Glide.with(this)
                .load(uri)
                .listener(glideListener)
                .into(photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            currentBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(currentBitmap);
            profilePresenter.savePhoto();
        } else if (requestCode == RESULT_PICK_PHOTO && resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            setPhoto(imageUri);
            currentBitmap = ((BitmapDrawable)photo.getDrawable()).getBitmap();
            profilePresenter.savePhoto();
        }
    }

    private RequestListener<Drawable> glideListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            showText(e.getMessage());
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            currentBitmap = ((BitmapDrawable) resource).getBitmap();
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profilePresenter.detachView();
    }
}

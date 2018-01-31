package com.mac.fireflies.wgt.createprofile;

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
import com.google.firebase.storage.FirebaseStorage;

public class ProfileActivity extends AppCompatActivity {
    EditText txtName;
    ImageView photo;
    Button bSave, bDelete;
    private Profile currentProfile;
    private FirebaseInteractor firebaseInteractor;
    private TextView txtEmail;
    static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtName = (EditText) findViewById(R.id.profile_name);
        photo = (ImageView) findViewById(R.id.profile_photo);
        txtEmail = (TextView) findViewById(R.id.profile_email);
        bSave = (Button) findViewById(R.id.profile_button_save);
        bDelete = (Button) findViewById(R.id.profile_button_delete);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrCreateProfile();
            }
        });
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProfile(currentProfile);
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        firebaseInteractor = FirebaseInteractor.getInstance();
        getData();
    }

    private void deleteProfile(Profile profile) {
        firebaseInteractor.deleteProfile(profile.getKey());
        finish();
    }

    private void updateOrCreateProfile() {
        checkCurrentProfile();
        if (txtName.getText() != null &&
                !txtName.getText().toString().equals(currentProfile.getName())) {
            currentProfile.setName(txtName.getText().toString());
            FirebaseInteractor.createOrUpdateProfile(currentProfile);
        }
        photo.setDrawingCacheEnabled(true);
        photo.buildDrawingCache();
        if (!currentProfile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            firebaseInteractor.savePhotoProfile(currentProfile, photo.getDrawingCache(), new FirebaseInteractor.FirebaseListener<Uri>() {
                @Override
                public void onResult(Uri result) {
                    showText(result.toString());
                }

                @Override
                public void onError(String error) {
                    showText(error);
                }
            });
        }
    }

    private void checkCurrentProfile() {
        if (currentProfile == null) {
            currentProfile = new Profile();
            currentProfile.setEmail(firebaseInteractor.getUserData().getEmail());
        }
    }

    private void getData() {
        FirebaseInteractor.getProfile(firebaseInteractor.getUserData().getEmail(), new FirebaseInteractor.FirebaseListener<Profile>() {
            @Override
            public void onResult(Profile result) {
                if (result != null) {
                    currentProfile = result;
                    displayProfile(currentProfile);
                } else {
                    displayUserData();
                }
            }

            @Override
            public void onError(String error) {
                showText(error);
            }
        });
    }

    private void showText(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void displayUserData() {
        txtEmail.setText(firebaseInteractor.getUserData().getEmail());
    }

    private void displayProfile(Profile profile) {
        if (profile.getName() != null) {
            txtName.setText(profile.getName());
        }
        txtEmail.setText(profile.getEmail());
        if (!profile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            firebaseInteractor.getUriPhotoProfile(profile, new FirebaseInteractor.FirebaseListener<Uri>() {
                @Override
                public void onResult(Uri result) {
                    Glide.with(getApplicationContext())
                            .load(result)
                            .into(photo);

                }

                @Override
                public void onError(String error) {
                    showText(error);
                }
            });
        }

    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);

            checkCurrentProfile();
            currentProfile.setPhoto(currentProfile.getKey() + ".bmp");
        }
    }
}

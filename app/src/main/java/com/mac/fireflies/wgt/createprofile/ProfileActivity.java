package com.mac.fireflies.wgt.createprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    EditText txtName;
    ImageView photo;
    Button bSave, bDelete;
    private Profile currentProfile;
    private FirebaseInteractor firebaseInteractor;
    private TextView txtEmail;

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
                firebaseInteractor.deleteProfile(currentProfile.getKey());
                finish();
            }
        });

        firebaseInteractor = FirebaseInteractor.getInstance();
        getData();
    }

    private void updateOrCreateProfile() {
        if (currentProfile == null) {
            currentProfile = new Profile();
            currentProfile.setEmail(firebaseInteractor.getUserData().getEmail());
        }
        if (txtName.getText() != null &&
                !txtName.getText().toString().equals(currentProfile.getName())) {
            currentProfile.setName(txtName.getText().toString());
            firebaseInteractor.createOrUpdateProfile(currentProfile);
        }
    }

    private void getData() {
        firebaseInteractor.getProfile(firebaseInteractor.getUserData().getEmail(), new FirebaseInteractor.FirebaseListener<Profile>() {
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
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserData() {
        txtEmail.setText(firebaseInteractor.getUserData().getEmail());
    }

    private void displayProfile(Profile profile) {
        if (profile.getName() != null) {
            txtName.setText(profile.getName());
        }
        txtEmail.setText(profile.getEmail());
    }
}

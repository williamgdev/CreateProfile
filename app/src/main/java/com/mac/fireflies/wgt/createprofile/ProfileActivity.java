package com.mac.fireflies.wgt.createprofile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

public class ProfileActivity extends AppCompatActivity {
    EditText txtName;
    ImageView photo;
    Button bSave;
    private Profile currentProfile;
    private FirebaseInteractor firebaseInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtName = (EditText) findViewById(R.id.profile_name);
        photo = (ImageView) findViewById(R.id.profile_photo);
        bSave = (Button) findViewById(R.id.profile_button_save);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrCreateProfile();
            }
        });

        firebaseInteractor = FirebaseInteractor.getInstance();
        getData();
    }

    private void updateOrCreateProfile() {
        if (txtName.getText() != null &&
                !txtName.getText().toString().equals(currentProfile.getName())) {
            currentProfile.setName(txtName.getText().toString());
            firebaseInteractor.createOrSaveProfile(currentProfile);
        }
    }

    private void getData() {
        Intent intent = getIntent();
        currentProfile = (Profile) intent.getSerializableExtra("profile");
    }
}

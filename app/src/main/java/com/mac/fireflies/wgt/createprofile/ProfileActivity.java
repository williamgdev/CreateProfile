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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getData();
        txtName = (EditText) findViewById(R.id.profile_name);
        photo = (ImageView) findViewById(R.id.profile_photo);
        bSave = (Button) findViewById(R.id.profile_button_save);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrCreateProfile();
            }
        });
    }

    private void updateOrCreateProfile() {
        if (txtName.getText() != null &&
                !txtName.getText().toString().equals(currentProfile.getName())) {
            currentProfile.setName(txtName.getText().toString());
            W2TUtil.createOrSaveProfile(currentProfile);
        }
    }

    private void getData() {
        Intent intent = getIntent();
        currentProfile = (Profile) intent.getSerializableExtra("profile");
        mDatabase = W2TUtil.getDatabase();
    }
}

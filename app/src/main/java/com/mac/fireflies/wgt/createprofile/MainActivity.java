package com.mac.fireflies.wgt.createprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtName, txtPhoto, txtEmail;
    Button bLogout, bLogIn, bMyProfile;
    private Profile currentProfile;
    private FirebaseInteractor firebaseInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseInteractor = FirebaseInteractor.getInstance();
        txtName = (TextView) findViewById(R.id.greetings);
        txtPhoto = (TextView) findViewById(R.id.photo);
        txtEmail = (TextView) findViewById(R.id.email);
        bLogIn = (Button) findViewById(R.id.button_logoin);
        bLogout = (Button) findViewById(R.id.button_logout);
        bMyProfile = (Button) findViewById(R.id.button_my_profile);
        bLogIn.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bMyProfile.setOnClickListener(this);

        firebaseInteractor.launchLogin(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FirebaseInteractor.RC_SIGN_IN && resultCode == RESULT_OK) {
            processData(data);

            bLogIn.setVisibility(View.GONE);
            bLogout.setVisibility(View.VISIBLE);
        } else {
            bLogout.setVisibility(View.GONE);
            bLogIn.setVisibility(View.VISIBLE);
        }
    }

    private void displayInfo(Profile profile) {
        txtName.setText("Hello! " + profile.getName());
        txtEmail.setText(profile.getEmail());
        txtPhoto.setText(profile.getPhoto());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                logout();
                bLogout.setVisibility(View.GONE);
                bLogIn.setVisibility(View.VISIBLE);
                break;
            case R.id.button_logoin:
                firebaseInteractor.launchLogin(this);
                bLogout.setVisibility(View.VISIBLE);
                bLogIn.setVisibility(View.GONE);
                break;
            case R.id.button_my_profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("profile", currentProfile);
                startActivity(intent);
                break;
        }
    }

    private void logout() {
        firebaseInteractor.logout(this, new FirebaseInteractor.FirebaseLogoutListener() {
            @Override
            public void onLogout() {
                txtName.setText("You has been singed out...");
            }
        });
    }

    private void processData(Intent data) {
        IdpResponse userData = IdpResponse.fromResultIntent(data);
        currentProfile = Profile.create(userData);

        firebaseInteractor.createOrSaveProfile(currentProfile);
        displayInfo(currentProfile);
    }
}

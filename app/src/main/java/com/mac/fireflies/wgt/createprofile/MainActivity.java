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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 123;
    private static final String DATABASE_NAME = "w2t_profile";
    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
    );

    TextView txtName, txtPhoto, txtEmail;
    Button bLogout, bLogIn;
    private DatabaseReference mDatabase;
    private Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = (TextView) findViewById(R.id.greetings);
        txtPhoto = (TextView) findViewById(R.id.photo);
        txtEmail = (TextView) findViewById(R.id.email);
        bLogIn = (Button) findViewById(R.id.button_logoin);
        bLogout = (Button) findViewById(R.id.button_logout);
        bLogIn.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        launchLogin();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(DATABASE_NAME);
    }

    public void launchLogin() {
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        txtName.setText("You has been singed out...");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            IdpResponse userData = IdpResponse.fromResultIntent(data);
            currentProfile = Profile.create(userData);

            saveProfile(currentProfile);
            displayInfo(currentProfile);

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

    private void saveProfile(Profile profile) {
        String key = W2TUtil.generateKey(profile.getEmail());
        mDatabase.child(key).setValue(profile.toMap());
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
                launchLogin();
                bLogout.setVisibility(View.VISIBLE);
                bLogIn.setVisibility(View.GONE);
                break;
        }
    }
}

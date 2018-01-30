package com.mac.fireflies.wgt.createprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.IdpResponse;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtName;
    Button bLogout, bLogIn, bMyProfile;
    private FirebaseInteractor firebaseInteractor;
    private IdpResponse userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseInteractor = FirebaseInteractor.getInstance();
        txtName = (TextView) findViewById(R.id.greetings);
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

    private void displayInfo(String email) {
        txtName.setText("Hello! " + email);
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
                firebaseInteractor.setUserData(userData);
                launchProfile();
                break;
        }
    }

    private void launchProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
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
        userData = IdpResponse.fromResultIntent(data);
        displayInfo(userData.getEmail());
    }
}

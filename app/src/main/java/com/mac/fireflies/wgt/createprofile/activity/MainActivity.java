package com.mac.fireflies.wgt.createprofile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.mac.fireflies.wgt.createprofile.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.ProfileActivity;
import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.MainPresenter;
import com.mac.fireflies.wgt.createprofile.view.MainView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView {


    TextView txtName;
    Button bLogout, bLogIn, bMyProfile;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.greetings);
        bLogIn = (Button) findViewById(R.id.button_logoin);
        bLogout = (Button) findViewById(R.id.button_logout);
        bMyProfile = (Button) findViewById(R.id.button_my_profile);
        bLogIn.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bMyProfile.setOnClickListener(this);

        mainPresenter= new MainPresenter();
        mainPresenter.attachView(this);
        this.login();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FirebaseInteractor.RC_SIGN_IN && resultCode == RESULT_OK) {
            mainPresenter.loadData(data);

            bLogIn.setVisibility(View.GONE);
            bLogout.setVisibility(View.VISIBLE);
        } else {
            bLogout.setVisibility(View.GONE);
            bLogIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayInfo(String email) {
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
                mainPresenter.login(this);
                bLogout.setVisibility(View.VISIBLE);
                bLogIn.setVisibility(View.GONE);
                break;
            case R.id.button_my_profile:
                mainPresenter.updateUserData();
                launchProfile();
                break;
        }
    }

    public void launchProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    public void logout() {
        mainPresenter.logout(this);
    }

    //
    @Override
    public void login() {
        mainPresenter.login(this);
    }

    @Override
    public void showText(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }
}

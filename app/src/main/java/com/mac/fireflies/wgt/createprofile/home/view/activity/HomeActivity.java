package com.mac.fireflies.wgt.createprofile.home.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.home.presenter.HomePresenter;
import com.mac.fireflies.wgt.createprofile.home.presenter.HomePresenterImpl;
import com.mac.fireflies.wgt.createprofile.home.view.HomeView;
import com.mac.fireflies.wgt.createprofile.profile.view.activity.ProfileActivity;
import com.mac.fireflies.wgt.createprofile.sign.view.activity.SignActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, HomeView {


    TextView txtName;
    Button bLogout, bMyProfile;
    HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtName = (TextView) findViewById(R.id.greetings);
        bLogout = (Button) findViewById(R.id.button_logout);
        bMyProfile = (Button) findViewById(R.id.button_my_profile);
        bLogout.setOnClickListener(this);
        bMyProfile.setOnClickListener(this);

        homePresenter = new HomePresenterImpl();
        homePresenter.attachView(this);

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setInfo(String email) {
        txtName.setText("Hello! " + email);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                logout();
                bLogout.setVisibility(View.GONE);
                launchLogin();
                break;
            case R.id.button_my_profile:
                launchProfile();
                break;
        }
    }

    @Override
    public void launchProfile() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void logout() {
        homePresenter.logout();
    }

    @Override
    public void login() {
        if (homePresenter.isUserLogged()) {
            homePresenter.displayLoggedUser();
            bLogout.setVisibility(View.VISIBLE);
        } else {
            launchLogin();
        }
    }

    @Override
    public void launchLogin() {
        homePresenter.login();
        Intent intent = new Intent(getApplicationContext(), SignActivity.class);
        startActivity(intent);
    }

    @Override
    public void showText(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (homePresenter != null) {
            this.login();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
    }
}

package com.mac.fireflies.wgt.createprofile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.MainPresenter;
import com.mac.fireflies.wgt.createprofile.presenter.MainPresenterImpl;
import com.mac.fireflies.wgt.createprofile.view.MainView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView {


    TextView txtName;
    Button bLogout, bMyProfile;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.greetings);
        bLogout = (Button) findViewById(R.id.button_logout);
        bMyProfile = (Button) findViewById(R.id.button_my_profile);
        bLogout.setOnClickListener(this);
        bMyProfile.setOnClickListener(this);

        mainPresenter= new MainPresenterImpl();
        mainPresenter.attachView(this);

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
        mainPresenter.logout();
    }

    @Override
    public void login() {
        if (mainPresenter.isUserLogged()) {
            mainPresenter.getCurrentUser();
            bLogout.setVisibility(View.VISIBLE);
        } else {
            launchLogin();
        }
    }

    @Override
    public void launchLogin() {
        mainPresenter.login();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
        if (mainPresenter != null) {
            this.login();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }
}

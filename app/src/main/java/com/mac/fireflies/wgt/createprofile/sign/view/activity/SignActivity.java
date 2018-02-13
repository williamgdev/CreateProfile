package com.mac.fireflies.wgt.createprofile.sign.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.view.fragment.SignInFragment;
import com.mac.fireflies.wgt.createprofile.sign.view.fragment.SignUpFragment;

/**
 * A login screen that offers login via email/password.
 */
public class SignActivity extends AppCompatActivity implements SignInFragment.OnFragmentInteractionListener, SignUpFragment.OnSignUpListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignInFragment fragment = SignInFragment.newInstance("", "");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_sign, fragment)
                .commit();
    }

    @Override
    public void onLoginSuccessful(String email) {
        Toast.makeText(this, "Here: " +email, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSignUpClicked() {
        SignUpFragment fragment = new SignUpFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_sign, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}


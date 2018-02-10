package com.mac.fireflies.wgt.createprofile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.fragment.SignInFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements SignInFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignInFragment fragment = SignInFragment.newInstance("", "");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_sign_in, fragment)
                .commit();
    }

    @Override
    public void onLoginSuccessful(String email) {
        Toast.makeText(this, "Here: " +email, Toast.LENGTH_SHORT).show();
        finish();
    }
}


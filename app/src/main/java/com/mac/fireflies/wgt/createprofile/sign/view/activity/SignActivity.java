package com.mac.fireflies.wgt.createprofile.sign.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.interactor.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;
import com.mac.fireflies.wgt.createprofile.sign.view.fragment.SignInFragment;
import com.mac.fireflies.wgt.createprofile.sign.view.fragment.SignUpFragment;

/**
 * A login screen that offers login via email/password.
 */
public class SignActivity extends AppCompatActivity implements SignInFragment.OnFragmentInteractionListener, SignUpFragment.OnSignUpListener {

    private static final int RC_SIGN_IN_GOOGLE = 600;
    private LinearLayout signInButtonsLayout;
    private AppCoreInteractor appCoreInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        signInButtonsLayout = findViewById(R.id.sign_in_buttons);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignInFragment();
            }
        });

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        appCoreInteractor = AppCoreInteractor.getInstance();
    }

    private void signInWithGoogle() {// Configure Google Sign In
        startActivityForResult(appCoreInteractor.getGoogleSignIntent(getApplicationContext()), RC_SIGN_IN_GOOGLE);
    }

    public void launchSignInFragment() {
        hideSignInLayout();
        SignInFragment fragment = SignInFragment.newInstance("", "");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_sign, fragment)
                .commit();
    }

    private void launchSignUpFragment() {
        hideSignInLayout();
        SignUpFragment fragment = new SignUpFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_sign, fragment)
                .commit();
    }

    public void hideSignInLayout() {
        signInButtonsLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            appCoreInteractor = AppCoreInteractor.getInstance();
            appCoreInteractor.getGoogleAccount(data, new AppCoreInteractor.AppCoreListener<GoogleSignInAccount>() {
                @Override
                public void onResult(GoogleSignInAccount result) {
                    appCoreInteractor.signInWithGoogle(
                            GoogleAuthProvider.getCredential(result.getIdToken(), null),
                            new AppCoreInteractor.AppCoreListener<W2TUser>() {
                                @Override
                                public void onResult(W2TUser result) {
                                    showToastAndClose(result);
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Google Sign In failed", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    @Override
    public void onSignUpClicked() {
        launchSignUpFragment();
    }

    @Override
    public void onSignUpSuccessful(W2TUser user) {
        showToastAndClose(user);
    }

    @Override
    public void onLoginSuccessful(W2TUser user) {
        showToastAndClose(user);
    }

    private void showToastAndClose(W2TUser user) {
        Toast.makeText(this, "Here: " + user.getName(), Toast.LENGTH_SHORT).show();
        finish();
    }
}


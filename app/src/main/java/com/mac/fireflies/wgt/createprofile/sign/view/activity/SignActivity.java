package com.mac.fireflies.wgt.createprofile.sign.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.view.SignView;
import com.mac.fireflies.wgt.createprofile.sign.view.fragment.SignEmailFragment;
import com.mac.fireflies.wgt.createprofile.sign.view.fragment.SignPhoneFragment;

/**
 * A login screen that offers login via email/password.
 */
public class SignActivity extends AppCompatActivity
        implements SignView, SignEmailFragment.OnSignWithEmailListener, SignPhoneFragment.OnSignPhoneFragmentListener{

    private static final int RC_SIGN_IN_GOOGLE = 600;
    private LinearLayout signInButtonsLayout;
    private AppCoreInteractor appCoreInteractor;
    private View signUpLinkLayout;


    protected View progressBar;
    private SignPresenter presenter;
    private boolean isSignUp;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        signInButtonsLayout = (LinearLayout) findViewById(R.id.sign_in_buttons);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignWithEmailFragment();
            }
        });

        signUpLinkLayout = findViewById(R.id.layout_signup_link);
        TextView signUpLink = (TextView) findViewById(R.id.sign_up_link);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignUp = true;
                launchSignWithEmailFragment();
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

        Button phoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        phoneSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignPhoneFragment();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        callbackManager = CallbackManager.Factory.create();
        LoginButton facebookLoginButton = findViewById(R.id.facebook_login_button);

        presenter = new SignPresenterImpl();
        presenter.attachView(this);

        appCoreInteractor = AppCoreInteractor.getInstance();
        appCoreInteractor.registerFacebookCallBack(facebookLoginButton, callbackManager, this, new AppCoreInteractor.AppCoreListener<User>() {
            @Override
            public void onResult(User result) {
                showToastAndClose();
            }

            @Override
            public void onError(String error) {
                showText(error);
            }
        });
    }

    @Override
    public void launchSignPhoneFragment() {
        hideSignUpLinkLayout();
        hideSignInLayout();
        SignPhoneFragment fragment = SignPhoneFragment.newInstance("", "");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_sign, fragment)
                .commit();
    }

    @Override
    public void signInWithGoogle() {// Configure Google Sign In
        startActivityForResult(appCoreInteractor.getGoogleSignIntent(getApplicationContext()), RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void launchSignWithEmailFragment() {
        hideSignInLayout();
        SignEmailFragment fragment = SignEmailFragment.newInstance(isSignUp);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_sign, fragment)
                .commit();
    }

    @Override
    public void hideSignUpLinkLayout() {
        signUpLinkLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideSignInLayout() {
        signInButtonsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLoginWithGoogleSuccessfull() {
        showToastAndClose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            presenter.loadData(data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSignUpSuccessful() {
        showToastAndClose();
    }

    @Override
    public void onSignInSuccessful() {
        showToastAndClose();
    }

    private void showToastAndClose() {
        Toast.makeText(this, "Welcome: " + appCoreInteractor.retrieveUserInfoByProvider(), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPhoneLoginSuccessful(User user) {
        showToastAndClose();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.container_layout).setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.container_layout).setVisibility(View.VISIBLE);
    }


    @Override
    public void setInfo(String email) {
        // @TODO Implement StatusBar
    }

    @Override
    public void showText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

}

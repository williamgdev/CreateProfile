package com.mac.fireflies.wgt.createprofile.core.interactor;


import android.app.Activity;
import android.app.Application;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;

public class FacebookInteractor {
    private static final FacebookInteractor ourInstance = new FacebookInteractor();

    private Profile profile;

    public static FacebookInteractor getInstance() {
        return ourInstance;
    }

    private FacebookInteractor() {

    }

    public void registerCallBack(LoginButton facebookLoginButton, CallbackManager callbackManager, Activity activity, final FacebookInteractorListener<AuthCredential> listener) {
//        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (isUserLogged()) {
                    // User Profile
                    profile = Profile.getCurrentProfile();

                    AuthCredential credential = FacebookAuthProvider.getCredential(AccessToken.getCurrentAccessToken().getToken());
                    listener.onResult(credential);
                } else {
                    listener.onError(loginResult.toString());
                }
            }

            @Override
            public void onCancel() {
                listener.onError("Facebook Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                listener.onError("Facebook Login found an error: " + error.toString());
            }
        });
    }

    public boolean isUserLogged() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public Profile getCurrentProfile() {
        return profile;
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    public interface FacebookInteractorListener<T> {
        void onResult(T result);
        void onError(String error);
    }
}

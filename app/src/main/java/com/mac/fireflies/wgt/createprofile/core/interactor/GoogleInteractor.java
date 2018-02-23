package com.mac.fireflies.wgt.createprofile.core.interactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mac.fireflies.wgt.createprofile.R;

/**
 * Created by willimail on 2/22/18.
 */

public class GoogleInteractor {
    private static final GoogleInteractor ourInstance = new GoogleInteractor();
    private GoogleSignInClient mGoogleSignInClient;

    public static GoogleInteractor getInstance() {
        return ourInstance;
    }

    private GoogleInteractor() {
    }

    public void getGoogleAccount(Intent intent, GoogleInteractorListener listener) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            listener.onResult(account);
        } catch (ApiException e) {
            listener.onError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void logout() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("GoogleInteractor", "onComplete: Logout with Google successfully");
                    }
                });
    }

    public Intent getSignIntent(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        return mGoogleSignInClient.getSignInIntent();
    }

    public interface GoogleInteractorListener{
        void onResult(GoogleSignInAccount account);

        void onError(String error);
    }
}

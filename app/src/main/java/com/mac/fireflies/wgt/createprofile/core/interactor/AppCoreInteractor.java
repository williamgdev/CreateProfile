package com.mac.fireflies.wgt.createprofile.core.interactor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;
import com.mac.fireflies.wgt.createprofile.profile.model.Profile;

/**
 * Created by willimail on 2/22/18.
 */

public class AppCoreInteractor {

    private static final AppCoreInteractor ourInstance = new AppCoreInteractor();

    private final GoogleInteractor googleInteractor;
    FirebaseInteractor firebaseInteractor;

    private AppCoreInteractor() {
        firebaseInteractor = FirebaseInteractor.getInstance();
        googleInteractor = GoogleInteractor.getInstance();
    }

    public static AppCoreInteractor getInstance() {
        return ourInstance;
    }

    public void logout() {
        googleInteractor.logout();
        firebaseInteractor.logout();
    }

    public boolean isUserLogged() {
        return firebaseInteractor.isUserLogged();
    }

    public W2TUser getCurrentUser() {
        return firebaseInteractor.getCurrentUser();
    }

    public void getUriPhotoProfile(Profile currentProfile, final AppCoreListener<Uri> appCoreListener) {
        firebaseInteractor.getUriPhotoProfile(currentProfile, new FirebaseInteractor.FirebaseListener<Uri>() {
            @Override
            public void onResult(Uri result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void getProfile(String email, final AppCoreListener<Profile> appCoreListener) {
        firebaseInteractor.getProfile(email, new FirebaseInteractor.FirebaseListener<Profile>() {
            @Override
            public void onResult(Profile result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void deleteProfile(String key, final AppCoreListener<String> appCoreListener) {
        firebaseInteractor.deleteProfile(key, new FirebaseInteractor.FirebaseListener<String>() {
            @Override
            public void onResult(String result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void createOrUpdateProfile(Profile currentProfile, final AppCoreListener<String> appCoreListener) {
        firebaseInteractor.createOrUpdateProfile(currentProfile, new FirebaseInteractor.FirebaseListener<String>() {
            @Override
            public void onResult(String result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void savePhotoProfile(Profile currentProfile, Bitmap currentBitmap, final AppCoreListener<Uri> appCoreListener) {
        firebaseInteractor.savePhotoProfile(currentProfile, currentBitmap, new FirebaseInteractor.FirebaseListener<Uri>() {
            @Override
            public void onResult(Uri result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void signIn(String email, String password, final AppCoreListener<W2TUser> appCoreListener) {
        firebaseInteractor.signIn(email, password, new FirebaseInteractor.FirebaseListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void signUp(String mEmail, String mPassword, final AppCoreListener<W2TUser> appCoreListener) {
        firebaseInteractor.signUp(mEmail, mPassword, new FirebaseInteractor.FirebaseListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void signInWithGoogle(AuthCredential credential, final AppCoreListener<W2TUser> appCoreListener) {
        firebaseInteractor.signInWithGoogle(credential, new FirebaseInteractor.FirebaseListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void getGoogleAccount(Intent intent, final AppCoreListener<GoogleSignInAccount> appCoreListener) {
        googleInteractor.getGoogleAccount(intent, new GoogleInteractor.GoogleInteractorListener() {
            @Override
            public void onResult(GoogleSignInAccount account) {
                appCoreListener.onResult(account);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public Intent getGoogleSignIntent(Context context) {
        return googleInteractor.getSignIntent(context);
    }

    public interface AppCoreListener<T> {
        void onResult(T result);
        void onError(String error);
    }
}

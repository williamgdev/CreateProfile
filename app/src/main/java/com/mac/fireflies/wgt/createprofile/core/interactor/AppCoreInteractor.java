package com.mac.fireflies.wgt.createprofile.core.interactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.mac.fireflies.wgt.createprofile.core.model.User;
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
        if (isGoogleProvider()) {
            googleInteractor.logout();
        }
        firebaseInteractor.logout();
    }

    private boolean isGoogleProvider() {
        return firebaseInteractor.getCurrentUser() != null && firebaseInteractor.getCurrentUser().getProvider().equals(User.PROVIDER_GOOGLE);
    }

    public boolean isUserLogged(Context context) {
        boolean userLogged = false;
        if (isGoogleProvider() && googleInteractor.isUserLogged(context)) {
            userLogged = true;
        }
        if (firebaseInteractor.isUserLogged()) {
            userLogged = true;
        }
        return userLogged;
    }

    public User getCurrentUser() {
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

    public void signIn(String email, String password, final AppCoreListener<User> appCoreListener) {
        firebaseInteractor.signIn(email, password, new FirebaseInteractor.FirebaseListener<User>() {
            @Override
            public void onResult(User result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void signUp(String mEmail, String mPassword, final AppCoreListener<User> appCoreListener) {
        firebaseInteractor.signUp(mEmail, mPassword, new FirebaseInteractor.FirebaseListener<User>() {
            @Override
            public void onResult(User result) {
                appCoreListener.onResult(result);
            }

            @Override
            public void onError(String error) {
                appCoreListener.onError(error);
            }
        });
    }

    public void signInWithGoogle(AuthCredential credential, final AppCoreListener<User> appCoreListener) {
        firebaseInteractor.signInWithGoogle(credential, new FirebaseInteractor.FirebaseListener<User>() {
            @Override
            public void onResult(User result) {
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

    public void signInWithPhone(String phoneNumber, Activity activity, final AppCoreListener<User> listener) {
        firebaseInteractor.signInWithPhone(phoneNumber, activity, new FirebaseInteractor.FirebaseListener<User>() {
            @Override
            public void onResult(User result) {
                listener.onResult(result);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    public void verifyPhoneCode(String code, final AppCoreListener<User> listener) {
        firebaseInteractor.verifyPhoneCode(code, new FirebaseInteractor.FirebaseListener<User>() {
            @Override
            public void onResult(User result) {
                listener.onResult(result);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    public interface AppCoreListener<T> {
        void onResult(T result);
        void onError(String error);
    }
}

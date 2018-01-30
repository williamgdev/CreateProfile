package com.mac.fireflies.wgt.createprofile;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wiltorri on 1/28/18.
 */

public class FirebaseInteractor {
    private static final FirebaseInteractor ourInstance = new FirebaseInteractor();

    public static final int RC_SIGN_IN = 123;
    private static final String DATABASE_NAME = "w2t_profile";

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
    );

    public static FirebaseInteractor getInstance() {
        return ourInstance;
    }

    private FirebaseInteractor() {
    }


    public void launchLogin(Activity activity) {
        // Create and launch sign-in intent
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference().child(DATABASE_NAME);
    }

    public static void createOrSaveProfile(Profile profile) {
        String key = W2TUtil.generateKey(profile.getEmail());
        getDatabase().child(key).setValue(profile.toMap());
    }


    public void logout(Activity activity, final FirebaseLogoutListener listener) {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onLogout();
                    }
                });
    }

    public interface FirebaseLogoutListener {
        void onLogout();
    }
}

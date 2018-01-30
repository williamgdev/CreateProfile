package com.mac.fireflies.wgt.createprofile;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private IdpResponse userData;

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

    public static void createOrUpdateProfile(Profile profile) {
        getDatabase().child(profile.getKey()).setValue(profile.toMap());
    }

    public static void getProfile(String email, final FirebaseListener<Profile> listener) {
        String key = W2TUtil.generateKey(email);
        getDatabase().child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Map<String, String> data = (Map<String, String>) dataSnapshot.getValue();
                    Profile profile = Profile.create(data);
                    listener.onResult(profile);
                } else {
                    listener.onResult(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
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

    public void setUserData(IdpResponse userData) {
        this.userData = userData;
    }

    public IdpResponse getUserData() {
        return userData;
    }

    public void deleteProfile(String key) {
        getDatabase().child(key).removeValue();
    }

    public interface FirebaseLogoutListener {
        void onLogout();
    }

    public interface FirebaseListener<T>{
        void onResult(T result);
        void onError(String error);
    }
}

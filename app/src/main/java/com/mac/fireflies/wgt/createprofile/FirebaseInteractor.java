package com.mac.fireflies.wgt.createprofile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private static final String IMAGES_PATH = "images/";

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

    public static StorageReference getStorage() {
        return FirebaseStorage.getInstance().getReference();
    }

    public void savePhotoProfile(Profile profile, Bitmap bitmap, final FirebaseListener<Uri> listener) {
        if (!profile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            StorageReference storage = getStorage().child(IMAGES_PATH + profile.getPhoto());
            UploadTask uploadTask = storage.putBytes(W2TUtil.convertImageFromBMPToByte(bitmap));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    listener.onResult(downloadUrl);
                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e.getMessage());
                }
            });
        }
    }

    public void getUriPhotoProfile(Profile profile, final FirebaseListener<Uri> listener) {
        Task<Uri> photoStorage = getStorage().child(IMAGES_PATH + profile.getPhoto()).getDownloadUrl();
        photoStorage.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                listener.onResult(uri);
            }
        });
        photoStorage.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getMessage());
            }
        });
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

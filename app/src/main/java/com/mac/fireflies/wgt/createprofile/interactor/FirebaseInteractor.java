package com.mac.fireflies.wgt.createprofile.interactor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mac.fireflies.wgt.createprofile.W2TUtil;
import com.mac.fireflies.wgt.createprofile.model.Profile;
import com.mac.fireflies.wgt.createprofile.model.W2TUser;

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
    private final FirebaseAuth mAuth;

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
    );
    private W2TUser currentUser;

    public static FirebaseInteractor getInstance() {
        return ourInstance;
    }

    private FirebaseInteractor() {
        mAuth = FirebaseAuth.getInstance();
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

    public static void createOrUpdateProfile(Profile profile, final FirebaseListener<String> listener) {
        getDatabase().child(profile.getKey())
                .setValue(profile.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onResult("The profile has been created successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.toString());
                    }
                });
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

    public void logout() {
        mAuth.signOut();
    }

    public void deleteProfile(String key, final FirebaseListener<String> listener) {
        getDatabase().child(key).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    listener.onResult("The profile has been deleted successfully.");
                } else {
                    listener.onError("Something went wrong, please try again...");
                }
            }
        });
    }

    public W2TUser getCurrentUser() {
        if (currentUser == null && mAuth.getCurrentUser() != null) {
            currentUser = W2TUser.create(mAuth.getCurrentUser());
            return currentUser;
        } else {
            return null;
        }
    }

    public boolean isUserLogged() {
        getCurrentUser();
        return currentUser != null;
    }

    public interface FirebaseListener<T>{
        void onResult(T result);
        void onError(String error);
    }
}

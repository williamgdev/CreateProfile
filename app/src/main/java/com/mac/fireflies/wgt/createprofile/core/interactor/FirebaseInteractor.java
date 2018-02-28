package com.mac.fireflies.wgt.createprofile.core.interactor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.core.util.W2TUtil;
import com.mac.fireflies.wgt.createprofile.profile.model.Profile;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wiltorri on 1/28/18.
 */

public class FirebaseInteractor {
    private static final FirebaseInteractor ourInstance = new FirebaseInteractor();

    public static final int RC_SIGN_IN = 123;
    private static final String DATABASE_NAME = "w2t_profile";
    private static final String IMAGES_PATH = "images/";
    private final FirebaseAuth mAuth;
    public String currentPhoneVerificationId;

    private User currentUser;

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
        FirebaseDatabase.getInstance().goOnline();
        return FirebaseDatabase.getInstance().getReference().child(DATABASE_NAME);
    }

    public  void createOrUpdateProfile(Profile profile, final FirebaseListener<String> listener) {
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

    public void getProfile(String email, final FirebaseListener<Profile> listener) {
        String key = W2TUtil.generateKey(email);
        //@TODO Check why DatabaseValueEventListener is not working offline
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
                if (databaseError.getCode() == -3) {
                    FirebaseDatabase.getInstance().goOffline();
                    return;
                }
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

    public User getCurrentUser() {
        if (mAuth.getCurrentUser() != null) {
            currentUser = User.create(mAuth.getCurrentUser());
            return currentUser;
        } else {
            return currentUser = null;
        }
    }

    public boolean isUserLogged() {
        getCurrentUser();
        return currentUser != null;
    }

    public void signIn(String email, String password, final FirebaseListener<User> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onResult(getCurrentUser());
                        } else {
                            listener.onError("Authentication failed.");
                        }

                    }
                });
    }

    public void signUp(String email, String password, final FirebaseListener<User> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = User.create(mAuth.getCurrentUser());
                            listener.onResult(currentUser);
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signInWithGoogle(AuthCredential credential, final FirebaseListener<User> listener) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = User.create(mAuth.getCurrentUser());
                            listener.onResult(currentUser);
                        } else {
                            listener.onError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signInWithPhone(String phoneNumber, Activity activity, final FirebaseListener<User> listener) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,           // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    currentUser = User.create(mAuth.getCurrentUser());
                                    listener.onResult(currentUser);
                                } else {
                                    listener.onError(task.getException().getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        listener.onError(e.getMessage());

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        listener.onError(s);
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        currentPhoneVerificationId = verificationId;
                    }
                });
    }
    
    public void verifyPhoneCode(String phoneCode, final FirebaseListener<User> listener) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(currentPhoneVerificationId, phoneCode);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = User.create(mAuth.getCurrentUser());
                    listener.onResult(currentUser);
                } else {
                    listener.onError(task.getException().getMessage());
                }
            }
        });
    }

    public interface FirebaseListener<T> {
        void onResult(T result);

        void onError(String error);
    }
}

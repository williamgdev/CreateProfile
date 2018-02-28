package com.mac.fireflies.wgt.createprofile.core.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by willimail on 2/5/18.
 */

public class User {
    public static final String PROVIDER_GOOGLE = "GOOGLE";
    public static final String PROVIDER_PASSWORD = "PASSWORD";
    public static final String PROVIDER_NONE = "NONE";
    public static final String PROVIDER_PHONE = "PHONE";
    private String email;
    private String name;
    private String UID;
    private Uri photoUrl;
    private String phoneNumber;
    private String provider;

    public static User create(FirebaseUser firebaseUser) {
        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());
        user.setUID(firebaseUser.getUid());
        user.setPhotoUrl(firebaseUser.getPhotoUrl());
        user.setPhoneNumber(firebaseUser.getPhoneNumber());
        if (firebaseUser.getProviders().size() > 0) {
            switch (firebaseUser.getProviders().get(0)) {
                case "google.com":
                    user.setProvider(PROVIDER_GOOGLE);
                    break;

                case "password":
                    user.setProvider(PROVIDER_PASSWORD);
                    break;

                case "phone":
                    user.setProvider(PROVIDER_PHONE);
                    user.setPhoneNumber(firebaseUser.getPhoneNumber());
                    break;

                default:
                    user.setProvider(PROVIDER_NONE);
                    break;
            }
        } else {
            user.setProvider(PROVIDER_NONE);
        }
        return user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}

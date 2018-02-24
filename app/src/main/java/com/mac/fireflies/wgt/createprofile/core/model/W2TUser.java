package com.mac.fireflies.wgt.createprofile.core.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by willimail on 2/5/18.
 */

public class W2TUser {
    public static final String PROVIDER_GOOGLE = "GOOGLE";
    public static final String PROVIDER_FIREBASE = "FIREBASE";
    public static final String PROVIDER_NONE = "NONE";
    private String email;
    private String name;
    private String UID;
    private Uri photoUrl;
    private String phoneNumber;
    private String provider;

    public static W2TUser create(FirebaseUser user) {
        W2TUser w2TUser = new W2TUser();
        w2TUser.setEmail(user.getEmail());
        w2TUser.setName(user.getDisplayName());
        w2TUser.setUID(user.getUid());
        w2TUser.setPhotoUrl(user.getPhotoUrl());
        w2TUser.setPhoneNumber(user.getPhoneNumber());
        if (user.getProviders().size() > 0) {
            switch (user.getProviders().get(0)) {
                case "google.com":
                    w2TUser.setProvider(PROVIDER_GOOGLE);
                    break;

                case "firebase":
                    w2TUser.setProvider(PROVIDER_FIREBASE);
                    break;
            }
        } else {
            w2TUser.setProvider(PROVIDER_NONE);
        }
        return w2TUser;
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

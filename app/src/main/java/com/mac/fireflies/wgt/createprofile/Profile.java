package com.mac.fireflies.wgt.createprofile;

import com.firebase.ui.auth.IdpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wiltorri on 1/27/18.
 */

public class Profile {
    private String name;
    private String email;
    private String photo;
    private String mToken;

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static Profile create(IdpResponse userData) {
        Profile profile = new Profile();
        profile.setEmail(userData.getEmail());
        profile.setmToken(userData.getIdpToken());
        return profile;
    }

    public Map<String, String> toMap() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("name", name);
        jsonMap.put("email", email);
        jsonMap.put("photo", photo);
        jsonMap.put("mToken", mToken);
        return jsonMap;
    }
}

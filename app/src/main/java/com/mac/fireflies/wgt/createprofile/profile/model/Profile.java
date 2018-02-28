package com.mac.fireflies.wgt.createprofile.profile.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mac.fireflies.wgt.createprofile.core.util.CoreUtil;


/**
 * Created by wiltorri on 1/27/18.
 */

public class Profile implements Serializable {
    private static final String PROFILE_NAME = "name";
    private static final String PROFILE_EMAIL = "email";
    private static final String PROFILE_PHOTO = "photo";
    private static final String PROFILE_KEY = "key";
    public static final String DEFAULT_PHOTO = "profile_default_pic.bmp";
    private String name;
    private String email;
    private String photo = DEFAULT_PHOTO;
    private String key;

    public String getKey() {
        if (key == null) {
            key = CoreUtil.generateKey(email);
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Map<String, String> toMap() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put(PROFILE_NAME, name);
        jsonMap.put(PROFILE_EMAIL, email);
        jsonMap.put(PROFILE_PHOTO, photo);
        jsonMap.put(PROFILE_KEY, key);
        return jsonMap;
    }

    public static Profile create(Map<String, String> data) {
        Profile profile = new Profile();
        if (data != null) {
            profile.setName(data.get(Profile.PROFILE_NAME));
            profile.setEmail(data.get(Profile.PROFILE_EMAIL));
            profile.setPhoto(data.get(Profile.PROFILE_PHOTO));
            profile.setKey(data.get(Profile.PROFILE_KEY));

        }
        return profile;
    }
}

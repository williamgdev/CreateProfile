package com.mac.fireflies.wgt.createprofile;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wiltorri on 1/27/18.
 */

class W2TUtil {
    public static String generateKey(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    private static final String DATABASE_NAME = "w2t_profile";
    public static DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference().child(DATABASE_NAME);
    }

    public static void createOrSaveProfile(Profile profile) {
        String key = W2TUtil.generateKey(profile.getEmail());
        getDatabase().child(key).setValue(profile.toMap());
    }
}

package com.mac.fireflies.wgt.createprofile.view;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by willimail on 2/2/18.
 */

public interface ProfileView extends BaseView {

    void pickPhotoFromLibrary();

    void setProfile(String name, String email);

    void takePicture();

    void finish();

    String getName();

    Bitmap getPhoto();

    void setPhoto(Uri uri);
}

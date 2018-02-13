package com.mac.fireflies.wgt.createprofile.profile.view;

import android.graphics.Bitmap;
import android.net.Uri;

import com.mac.fireflies.wgt.createprofile.BaseView;

/**
 * Created by willimail on 2/2/18.
 */

public interface ProfileView extends BaseView {

    void pickPhotoFromLibrary();

    void setProfile(String name, String email);

    void takePicture();

    void finish();

    String getName();

    Bitmap getCurrentBitmap();

    void setPhoto(Uri uri);
}

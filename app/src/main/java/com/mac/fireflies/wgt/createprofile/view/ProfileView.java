package com.mac.fireflies.wgt.createprofile.view;

import android.graphics.Bitmap;
import android.net.Uri;

import com.mac.fireflies.wgt.createprofile.Profile;

/**
 * Created by willimail on 2/2/18.
 */

public interface ProfileView extends BaseView {

    void setProfile(Profile profile);

    void takePicture();

    void finish();

    String getName();

    Bitmap getPhoto();

    void setPhoto(Uri uri);
}

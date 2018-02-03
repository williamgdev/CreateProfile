package com.mac.fireflies.wgt.createprofile.presenter;

import com.mac.fireflies.wgt.createprofile.view.ProfileView;

/**
 * Created by willimail on 2/2/18.
 */

public interface ProfilePresenter extends BasePresenter<ProfileView> {
    void loadPhoto();

    void deleteProfile();

    void updateOrCreateProfile();

    void checkCurrentProfile();

    void savePhoto();
}

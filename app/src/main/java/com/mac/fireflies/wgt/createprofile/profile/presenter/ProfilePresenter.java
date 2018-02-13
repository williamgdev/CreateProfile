package com.mac.fireflies.wgt.createprofile.profile.presenter;


import com.mac.fireflies.wgt.createprofile.BasePresenter;
import com.mac.fireflies.wgt.createprofile.profile.view.ProfileView;

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

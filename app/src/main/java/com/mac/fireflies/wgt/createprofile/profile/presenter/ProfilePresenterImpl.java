package com.mac.fireflies.wgt.createprofile.profile.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.interactor.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.profile.model.Profile;
import com.mac.fireflies.wgt.createprofile.profile.view.ProfileView;

/**
 * Created by willimail on 2/2/18.
 */

public class ProfilePresenterImpl implements ProfilePresenter {
    private static final String TAG = "Profile =>";
    private ProfileView profileView;

    private Profile currentProfile;
    private AppCoreInteractor appCoreInteractor;

    @Override
    public void attachView(ProfileView view) {
        this.profileView = view;
        appCoreInteractor = AppCoreInteractor.getInstance();
    }

    @Override
    public void detachView() {
        profileView = null;
    }

    @Override
    public void loadData(Intent data) {
        appCoreInteractor.getProfile(appCoreInteractor.getCurrentUser().getEmail(), new AppCoreInteractor.AppCoreListener<Profile>() {
            @Override
            public void onResult(Profile result) {
                if (result != null) {
                    currentProfile = result;
                    profileView.setProfile(currentProfile.getName(), currentProfile.getEmail());
                    loadPhoto();
                } else {
                    profileView.setInfo(appCoreInteractor.getCurrentUser().getEmail());
                }
            }

            @Override
            public void onError(String error) {
                if (profileView != null) {
                    profileView.showText(error);
                } else {
                    Log.d(TAG, "onError: " + error);
                }
            }
        });
    }

    @Override
    public void getCurrentUser() {
        profileView.setInfo(appCoreInteractor.getCurrentUser().getEmail());
    }

    @Override
    public void loadPhoto() {
        if (!currentProfile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            appCoreInteractor.getUriPhotoProfile(currentProfile, new AppCoreInteractor.AppCoreListener<Uri>() {
                @Override
                public void onResult(Uri result) {
                    profileView.setPhoto(result);
                }

                @Override
                public void onError(String error) {
                    profileView.showText(error);
                }
            });
        }
    }

    @Override
    public void deleteProfile() {
        appCoreInteractor.deleteProfile(currentProfile.getKey(), new AppCoreInteractor.AppCoreListener<String>() {
            @Override
            public void onResult(String result) {
                profileView.showText(result);
                profileView.finish();
            }

            @Override
            public void onError(String error) {
                profileView.showText(error);
                profileView.finish();

            }
        });
    }

    @Override
    public void updateOrCreateProfile() {
        checkCurrentProfile();
        if (profileView.getName() != null &&
                !profileView.getName().equals(currentProfile.getName())) {
            currentProfile.setName(profileView.getName());
            appCoreInteractor.createOrUpdateProfile(currentProfile, new AppCoreInteractor.AppCoreListener<String>() {
                @Override
                public void onResult(String result) {
                    profileView.showText(result);
                }

                @Override
                public void onError(String error) {
                    profileView.showText(error);
                }
            });
        }
        if (!currentProfile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            savePhoto();
        }
    }

    @Override
    public void checkCurrentProfile() {
        if (currentProfile == null) {
            currentProfile = new Profile();
            currentProfile.setEmail(appCoreInteractor.getCurrentUser().getEmail());
        }
    }

    @Override
    public void savePhoto() {
        checkCurrentProfile();
        currentProfile.setPhoto(currentProfile.getKey() + ".bmp");
        appCoreInteractor.savePhotoProfile(currentProfile, profileView.getCurrentBitmap(), new AppCoreInteractor.AppCoreListener<Uri>() {
            @Override
            public void onResult(Uri result) {
                profileView.showText(result.toString());
            }

            @Override
            public void onError(String error) {
                profileView.showText(error);
            }
        });

    }
}

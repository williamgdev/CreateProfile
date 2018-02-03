package com.mac.fireflies.wgt.createprofile.presenter;

import android.content.Intent;
import android.net.Uri;

import com.mac.fireflies.wgt.createprofile.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.Profile;
import com.mac.fireflies.wgt.createprofile.view.ProfileView;

/**
 * Created by willimail on 2/2/18.
 */

public class ProfilePresenterImpl implements ProfilePresenter {
    private ProfileView profileView;

    private Profile currentProfile;
    private FirebaseInteractor firebaseInteractor;

    @Override
    public void attachView(ProfileView view) {
        this.profileView = view;
        firebaseInteractor = FirebaseInteractor.getInstance();
    }

    @Override
    public void detachView() {
        profileView = null;
    }

    @Override
    public void loadData(Intent data) {
        FirebaseInteractor.getProfile(firebaseInteractor.getUserData().getEmail(), new FirebaseInteractor.FirebaseListener<Profile>() {
            @Override
            public void onResult(Profile result) {
                if (result != null) {
                    currentProfile = result;
                    profileView.setProfile(currentProfile);
                    loadPhoto();
                } else {
                    profileView.setInfo(firebaseInteractor.getUserData().getEmail());
                }
            }

            @Override
            public void onError(String error) {
                profileView.showText(error);
            }
        });
    }

    private void loadPhoto() {
        if (!currentProfile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            firebaseInteractor.getUriPhotoProfile(currentProfile, new FirebaseInteractor.FirebaseListener<Uri>() {
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
        firebaseInteractor.deleteProfile(currentProfile.getKey());
        profileView.finish();

    }

    @Override
    public void updateOrCreateProfile() {
        checkCurrentProfile();
        if (profileView.getName() != null &&
                !profileView.getName().toString().equals(currentProfile.getName())) {
            currentProfile.setName(profileView.getName().toString());
            FirebaseInteractor.createOrUpdateProfile(currentProfile);
        }

        if (!currentProfile.getPhoto().equals(Profile.DEFAULT_PHOTO)) {
            savePhoto();
        }
    }

    @Override
    public void checkCurrentProfile() {
        if (currentProfile == null) {
            currentProfile = new Profile();
            currentProfile.setEmail(firebaseInteractor.getUserData().getEmail());
        }
    }

    @Override
    public void savePhoto() {
        checkCurrentProfile();
        currentProfile.setPhoto(currentProfile.getKey() + ".bmp");
        firebaseInteractor.savePhotoProfile(currentProfile, profileView.getPhoto(), new FirebaseInteractor.FirebaseListener<Uri>() {
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

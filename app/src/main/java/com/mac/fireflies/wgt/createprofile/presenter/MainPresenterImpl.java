package com.mac.fireflies.wgt.createprofile.presenter;

import android.content.Intent;

import com.firebase.ui.auth.IdpResponse;
import com.mac.fireflies.wgt.createprofile.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.activity.MainActivity;
import com.mac.fireflies.wgt.createprofile.view.MainView;

/**
 * Created by Alestar on 2/1/2018.
 */

public class MainPresenterImpl implements MainPresenter {


    private FirebaseInteractor firebaseInteractor;
    private IdpResponse userData;
    private MainView view;

    @Override
    public void login() {
        this.firebaseInteractor.launchLogin(view.getActivity());
    }

    @Override
    public void updateUserData() {
        this.firebaseInteractor.setUserData(userData);
    }

    @Override
    public void logout() {
        firebaseInteractor.logout(view.getActivity(), new FirebaseInteractor.FirebaseLogoutListener() {
            @Override
            public void onLogout() {
                view.showText("You has been singed out...");
            }
        });
    }

    @Override
    public void loadData(Intent data) {
        if (data != null) {
            this.userData = IdpResponse.fromResultIntent(data);
            firebaseInteractor.setUserData(userData);
            view.setInfo(userData.getEmail());
        }
        else
            view.showText("Something went wrong on loading user data");


    }

    @Override
    public void attachView(MainView view) {
        this.view=view;
        this.firebaseInteractor = FirebaseInteractor.getInstance();

    }

    @Override
    public void detachView() {
        this.view=null;
    }
}

package com.mac.fireflies.wgt.createprofile.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.interactor.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.view.MainView;

/**
 * Created by Alestar on 2/1/2018.
 */

public class MainPresenterImpl implements MainPresenter {


    private FirebaseInteractor firebaseInteractor;
    private MainView view;

    @Override
    public void login() {
        // @Todo do some process for login
    }

    @Override
    public void logout() {
        firebaseInteractor.logout();
        view.showText("You has been singed out...");
    }

    @Override
    public boolean isUserLogged() {
        return firebaseInteractor.isUserLogged();
    }

    @Override
    public void loadData(Intent data) {
//  @TODO Check if still need this method
//        if (data != null) {
//            this.userData = data.getSerializableExtra("account");
//            firebaseInteractor.setUserData(userData);
//            view.setInfo(userData.getEmail());
//        }
//        else {
//            view.showText("Something went wrong on loading user data");
//        }
    }

    @Override
    public void getCurrentUser() {
        view.setInfo(firebaseInteractor.getCurrentUser().getEmail());
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

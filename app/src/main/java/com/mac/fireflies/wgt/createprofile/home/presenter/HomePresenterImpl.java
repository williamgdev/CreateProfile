package com.mac.fireflies.wgt.createprofile.home.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.home.view.HomeView;
import com.mac.fireflies.wgt.createprofile.interactor.FirebaseInteractor;

/**
 * Created by Alestar on 2/1/2018.
 */

public class HomePresenterImpl implements HomePresenter {


    private FirebaseInteractor firebaseInteractor;
    private HomeView view;

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
    public void attachView(HomeView view) {
        this.view=view;
        this.firebaseInteractor = FirebaseInteractor.getInstance();

    }

    @Override
    public void detachView() {
        this.view=null;
    }
}

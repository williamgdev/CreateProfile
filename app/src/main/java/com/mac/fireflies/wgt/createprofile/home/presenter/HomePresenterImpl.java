package com.mac.fireflies.wgt.createprofile.home.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.home.view.HomeView;
import com.mac.fireflies.wgt.createprofile.core.interactor.FirebaseInteractor;

/**
 * Created by Alestar on 2/1/2018.
 */

public class HomePresenterImpl implements HomePresenter {


    private AppCoreInteractor appCoreInteractor;
    private HomeView view;

    @Override
    public void login() {
        // @Todo do some process for login
    }

    @Override
    public void logout() {
        appCoreInteractor.logout();
        view.showText("You has been singed out...");
    }

    @Override
    public boolean isUserLogged() {
        return appCoreInteractor.isUserLogged();
    }

    @Override
    public void loadData(Intent data) {
//  @TODO Check if still need this method
//        if (data != null) {
//            this.userData = data.getSerializableExtra("account");
//            appCoreInteractor.setUserData(userData);
//            view.setInfo(userData.getEmail());
//        }
//        else {
//            view.showText("Something went wrong on loading user data");
//        }
    }

    @Override
    public void getCurrentUser() {
        view.setInfo(appCoreInteractor.getCurrentUser().getEmail());
    }

    @Override
    public void attachView(HomeView view) {
        this.view=view;
        this.appCoreInteractor = AppCoreInteractor.getInstance();

    }

    @Override
    public void detachView() {
        this.view=null;
    }
}

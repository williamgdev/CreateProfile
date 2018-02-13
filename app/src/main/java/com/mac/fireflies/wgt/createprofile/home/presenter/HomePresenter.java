package com.mac.fireflies.wgt.createprofile.home.presenter;


import com.mac.fireflies.wgt.createprofile.BasePresenter;
import com.mac.fireflies.wgt.createprofile.home.view.HomeView;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface HomePresenter extends BasePresenter<HomeView> {

    void login();

    void logout();

    boolean isUserLogged();
}

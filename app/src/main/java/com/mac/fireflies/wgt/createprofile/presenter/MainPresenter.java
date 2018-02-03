package com.mac.fireflies.wgt.createprofile.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.activity.MainActivity;
import com.mac.fireflies.wgt.createprofile.view.MainView;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface MainPresenter extends BasePresenter<MainView> {

    void login();

    void updateUserData();

    void logout();
}

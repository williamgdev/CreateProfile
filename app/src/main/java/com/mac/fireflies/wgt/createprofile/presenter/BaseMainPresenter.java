package com.mac.fireflies.wgt.createprofile.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.activity.MainActivity;
import com.mac.fireflies.wgt.createprofile.view.MainView;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface BaseMainPresenter extends BasePresenter<MainView> {

    void loadData(Intent data);

    void login(MainActivity mainActivity);

    void updateUserData();

    void logout(MainActivity mainActivity);
}

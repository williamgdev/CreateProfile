package com.mac.fireflies.wgt.createprofile.core;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface BaseView {

    void showProgress();


    void hideProgress();


    void setInfo(String email);


    void showText(String text);


    Activity getActivity();
}

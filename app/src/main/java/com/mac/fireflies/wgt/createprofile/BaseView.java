package com.mac.fireflies.wgt.createprofile;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface BaseView {

    void showProgress(boolean b);


    void hideProgress();


    void setInfo(String email);


    void showText(String text);


    Activity getActivity();
}
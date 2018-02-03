package com.mac.fireflies.wgt.createprofile.view;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface BaseView {

    //void showProgress(){}


    //void hideProfress(){}


    void setInfo(String email);


    void showText(String text);


    Activity getActivity();
}

package com.mac.fireflies.wgt.createprofile.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.view.BaseView;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

    void loadData(Intent data);

    void getCurrentUser();
}

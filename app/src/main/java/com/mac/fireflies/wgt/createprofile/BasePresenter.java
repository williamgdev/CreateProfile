package com.mac.fireflies.wgt.createprofile;

import android.content.Intent;

/**
 * Created by Alestar on 2/1/2018.
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

    void loadData(Intent data);

    void getCurrentUser();
}

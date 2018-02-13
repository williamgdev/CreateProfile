package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.os.Bundle;

/**
 * Created by willimail on 2/12/18.
 */

public interface SignFragmentPresenter {
    void loadData(Bundle arguments);

    void populateAutoComplete();

    void setCredentials(String email, String password);

    boolean isEmailValid(String email);

    boolean isPasswordValid(String password);
}

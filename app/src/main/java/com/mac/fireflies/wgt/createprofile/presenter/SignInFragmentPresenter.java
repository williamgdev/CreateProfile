package com.mac.fireflies.wgt.createprofile.presenter;

import android.os.Bundle;

import com.mac.fireflies.wgt.createprofile.view.SignInFragmentView;

/**
 * Created by willimail on 2/10/18.
 */

public interface SignInFragmentPresenter extends BasePresenter<SignInFragmentView>{
    void loadData(Bundle arguments);

    void populateAutoComplete();

    void attemptLogin();

    void setCredentials(String s, String s1);

    void signIn(String email, String password);

    boolean isPasswordValid(String password);

    boolean isEmailValid(String email);
}

package com.mac.fireflies.wgt.createprofile.sign.presenter;

import com.mac.fireflies.wgt.createprofile.BasePresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.view.SignInFragmentView;

/**
 * Created by willimail on 2/10/18.
 */

public interface SignInFragmentPresenter extends BasePresenter<SignInFragmentView>, SignFragmentPresenter {

    void attemptLogin();

    void signIn(String email, String password);
}
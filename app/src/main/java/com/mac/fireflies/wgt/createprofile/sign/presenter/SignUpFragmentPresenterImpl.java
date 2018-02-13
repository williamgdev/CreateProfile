package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.sign.view.SignFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.SignUpFragmentView;

/**
 * Created by willimail on 2/12/18.
 */

public class SignUpFragmentPresenterImpl extends SignFragmentPresenterAbst implements SignUpFragmentPresenter {
    SignUpFragmentView view;

    @Override
    protected SignFragmentView getView() {
        return view;
    }

    @Override
    public void attachView(SignUpFragmentView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadData(Intent data) {

    }

    @Override
    public void getCurrentUser() {

    }

    @Override
    public void attemptSignUp() {

    }
}

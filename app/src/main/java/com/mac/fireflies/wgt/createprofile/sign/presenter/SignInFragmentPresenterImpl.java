package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.sign.view.SignFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.SignInFragmentView;

/**
 * Created by willimail on 2/10/18.
 */

public class SignInFragmentPresenterImpl extends SignFragmentPresenterAbst implements SignInFragmentPresenter {
    private SignInFragmentView view;


    private AppCoreInteractor appCoreInteractor;

    @Override
    public void attachView(SignInFragmentView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadData(Intent data) {
        // @TODO Method is not used
    }

    @Override
    public void displayLoggedUser() {
        // @TODO Method is not used

    }

    @Override
    public void signIn(String email, String password) {
        appCoreInteractor = AppCoreInteractor.getInstance();
        appCoreInteractor.signIn(email, password, new AppCoreInteractor.AppCoreListener<User>() {
            @Override
            public void onResult(User result) {
                view.hideProgress();
                view.onLoginSuccessful(result);
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
            }
        });
    }

    @Override
    public void setFields(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected SignFragmentView getView() {
        return view;
    }

    @Override
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        view.resetError();

        view.sendFieldsToPresenter();

        if(view.isCredentialsValid()) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            view.showProgress(true);
            signIn(mEmail, mPassword);
        }
    }

}

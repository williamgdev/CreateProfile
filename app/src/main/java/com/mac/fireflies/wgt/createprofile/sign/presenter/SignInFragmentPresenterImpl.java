package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.interactor.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.model.W2TUser;
import com.mac.fireflies.wgt.createprofile.sign.view.SignFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.SignInFragmentView;

/**
 * Created by willimail on 2/10/18.
 */

public class SignInFragmentPresenterImpl extends SignFragmentPresenterAbst implements SignInFragmentPresenter {
    private SignInFragmentView view;


    private FirebaseInteractor firebaseInteractor;

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
    public void getCurrentUser() {
        // @TODO Method is not used

    }

    @Override
    public void signIn(String email, String password) {
        firebaseInteractor = FirebaseInteractor.getInstance();
        firebaseInteractor.signIn(email, password, new FirebaseInteractor.FirebaseListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                view.showProgress(false);
                view.onLoginSuccessful(result.getEmail());
            }

            @Override
            public void onError(String error) {
                view.showProgress(false);
            }
        });
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

        view.sendCredentialsToPresenter();

        if(view.isCredentialsValid()) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            view.showProgress(true);
            signIn(mEmail, mPassword);
        }
    }

}
package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.interactor.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;
import com.mac.fireflies.wgt.createprofile.sign.view.SignFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.SignUpFragmentView;

/**
 * Created by willimail on 2/12/18.
 */

public class SignUpFragmentPresenterImpl extends SignFragmentPresenterAbst implements SignUpFragmentPresenter {
    SignUpFragmentView view;
    private String mConfirmationPassword;
    private String mFirstName;
    private String mLastName;
    private AppCoreInteractor appCoreInteractor;

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
    public void displayLoggedUser() {

    }

    @Override
    public void attemptSignUp() {
        view.resetError();

        view.sendFieldsToPresenter();

        if (view.isSignUpFormValid()) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            view.showProgress(true);
            signUp();
        }
    }

    private void signUp() {
        appCoreInteractor = AppCoreInteractor.getInstance();
        appCoreInteractor.signUp(mEmail, mPassword, new AppCoreInteractor.AppCoreListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                view.showProgress(true);
                view.onSingUpSuccessful(result);
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
                view.showText(error);
            }
        });
    }

    @Override
    public boolean isConfirmationPasswordValid(String confirmationPassword) {
        return mPassword.equals(confirmationPassword);
    }

    @Override
    public void sendFields(String email, String password, String confirmationPassword, String firstName, String lastName) {
        mEmail = email;
        mPassword = password;
        mConfirmationPassword = confirmationPassword;
        mFirstName = firstName;
        mLastName = lastName;
    }
}

package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;
import com.mac.fireflies.wgt.createprofile.sign.view.SignPhoneFragmentView;

/**
 * Created by willimail on 2/25/18.
 */

public class SignPhonePresenterImpl implements SignPhonePresenter {
    private SignPhoneFragmentView view;
    private AppCoreInteractor appCoreInteractor;

    @Override
    public void attachView(SignPhoneFragmentView view) {
        this.view = view;
        appCoreInteractor = AppCoreInteractor.getInstance();
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadData(Intent data) {

    }

    @Override
    public void displayLoggedUser() {

    }

    @Override
    public void sendVerificationCode(String phoneNumber) {
        appCoreInteractor.signInWithPhone(phoneNumber, view.getActivity(), new AppCoreInteractor.AppCoreListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                view.onLoginSuccessful(result);
            }

            @Override
            public void onError(String error) {
                view.showText(error);
            }
        });
    }

    @Override
    public void verifyCode(String code) {

    }
}

package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.content.Intent;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
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
        view.showProgress(true);
        appCoreInteractor.signInWithPhone(phoneNumber, view.getActivity(), signPhoneListener, new AppCoreInteractor.SentCodeListener() {
            @Override
            public void onCodeSent() {
                view.hideProgress();
            }
        });
    }

    @Override
    public void verifyCode(String code) {
        appCoreInteractor.verifyPhoneCode(code, signPhoneListener);
    }

    private AppCoreInteractor.AppCoreListener<User> signPhoneListener = new AppCoreInteractor.AppCoreListener<User>() {
        @Override
        public void onResult(User result) {
            view.onLoginSuccessful(result);
            view.hideProgress();
        }

        @Override
        public void onError(String error) {
            view.showText(error);
            view.hideProgress();
        }
    };
}

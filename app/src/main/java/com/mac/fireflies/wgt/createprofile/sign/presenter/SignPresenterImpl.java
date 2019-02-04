package com.mac.fireflies.wgt.createprofile.sign.presenter;


import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.sign.view.SignView;

public class SignPresenterImpl implements SignPresenter {
    private SignView view;

    @Override
    public void attachView(SignView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadData(Intent data) {
        view.showProgress();
        final AppCoreInteractor appCoreInteractor = AppCoreInteractor.getInstance();
        appCoreInteractor.getGoogleAccount(data, new AppCoreInteractor.AppCoreListener<GoogleSignInAccount>() {
            @Override
            public void onResult(GoogleSignInAccount result) {
                appCoreInteractor.signInWithSDKCredentials(
                        GoogleAuthProvider.getCredential(result.getIdToken(), null),
                        new AppCoreInteractor.AppCoreListener<User>() {
                            @Override
                            public void onResult(User result) {
                                view.setInfo(appCoreInteractor.retrieveUserInfoByProvider());
                                view.onLoginWithGoogleSuccessfull();
                                view.hideProgress();
                            }

                            @Override
                            public void onError(String error) {
                                view.showText(error);
                                view.hideProgress();
                            }
                        });
            }

            @Override
            public void onError(String error) {
                view.showText("Google Sign In failed");
                view.hideProgress();
            }
        });
    }

    @Override
    public void displayLoggedUser() {

    }
}

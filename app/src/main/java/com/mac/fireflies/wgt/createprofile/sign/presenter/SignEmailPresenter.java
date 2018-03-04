package com.mac.fireflies.wgt.createprofile.sign.presenter;


import android.os.Bundle;

import com.mac.fireflies.wgt.createprofile.core.BasePresenter;
import com.mac.fireflies.wgt.createprofile.sign.view.SignEmailFragmentView;

import java.util.Map;

public interface SignEmailPresenter extends BasePresenter<SignEmailFragmentView> {
    String FIELD_EMAIL = "EMAIL";
    String FIELD_PASSWORD = "PASSWORD";
    String FIELD_CONFIRM_PASSWORD = "CONFIRM_PASSWORD";
    String FIELD_FIRST_NAME = "FIRST_NAME";
    String FIELD_LAST_NAME = "LAST_NAME";

    boolean isSignUp();

    void loadData(Bundle arguments);

    void populateAutoComplete();

    boolean isEmailValid(String email);

    boolean isPasswordValid(String password);

    void attemptSignUp();

    void attemptLogin();

    boolean isCredentialsValid();

    boolean isConfirmAndFullNameValid();

    void setFields(Map<String, String> fields);

    boolean isConfirmationPasswordValid(String confirmationPassword);

    void signIn(String email, String password);
}

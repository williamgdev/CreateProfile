package com.mac.fireflies.wgt.createprofile.sign.presenter;

import com.mac.fireflies.wgt.createprofile.core.BasePresenter;
import com.mac.fireflies.wgt.createprofile.sign.view.SignUpFragmentView;

/**
 * Created by willimail on 2/12/18.
 */

public interface SignUpFragmentPresenter extends BasePresenter<SignUpFragmentView>, SignFragmentPresenter {
    void attemptSignUp();

    boolean isConfirmationPasswordValid(String confirmationPassword);

    void sendFields(String email, String password, String confirmationPassword, String firstName, String lastName);
}

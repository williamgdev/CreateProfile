package com.mac.fireflies.wgt.createprofile.sign.presenter;

import com.mac.fireflies.wgt.createprofile.core.BasePresenter;
import com.mac.fireflies.wgt.createprofile.sign.view.SignPhoneFragmentView;

/**
 * Created by willimail on 2/25/18.
 */

public interface SignPhonePresenter extends BasePresenter<SignPhoneFragmentView> {
    void sendVerificationCode(String phoneNumber);

    void verifyCode(String code);
}

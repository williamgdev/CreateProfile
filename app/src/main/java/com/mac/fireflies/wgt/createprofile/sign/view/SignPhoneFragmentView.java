package com.mac.fireflies.wgt.createprofile.sign.view;

import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;

/**
 * Created by willimail on 2/25/18.
 */

public interface SignPhoneFragmentView extends SignView{
    void phoneCodeAction();

    void onLoginSuccessful(W2TUser result);
}

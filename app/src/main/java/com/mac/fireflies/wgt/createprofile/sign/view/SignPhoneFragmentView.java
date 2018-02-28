package com.mac.fireflies.wgt.createprofile.sign.view;

import com.mac.fireflies.wgt.createprofile.core.model.User;

/**
 * Created by willimail on 2/25/18.
 */

public interface SignPhoneFragmentView extends SignView{
    void phoneCodeAction();

    void onLoginSuccessful(User result);
}

package com.mac.fireflies.wgt.createprofile.sign.view;

import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;

/**
 * Created by willimail on 2/10/18.
 */

public interface SignInFragmentView extends SignFragmentView {

    void onLoginSuccessful(W2TUser user);

    void signIn(String email, String password);
}

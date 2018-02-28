package com.mac.fireflies.wgt.createprofile.sign.view;

import com.mac.fireflies.wgt.createprofile.core.model.User;

/**
 * Created by willimail on 2/10/18.
 */

public interface SignInFragmentView extends SignFragmentView {

    void onLoginSuccessful(User user);

    void signIn(String email, String password);
}

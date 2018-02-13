package com.mac.fireflies.wgt.createprofile.sign.view;

/**
 * Created by willimail on 2/10/18.
 */

public interface SignInFragmentView extends SignFragmentView {

    void onLoginSuccessful(String emailLogged);

    void signIn(String email, String password);
}

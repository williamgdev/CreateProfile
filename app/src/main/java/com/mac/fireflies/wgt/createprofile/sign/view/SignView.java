package com.mac.fireflies.wgt.createprofile.sign.view;


import android.widget.ArrayAdapter;

import com.mac.fireflies.wgt.createprofile.core.BaseView;

/**
 * Created by willimail on 2/5/18.
 */

public interface SignView extends BaseView {

    void launchSignPhoneFragment();

    void signInWithGoogle();

    void launchSignWithEmailFragment();

    void hideSignUpLinkLayout();

    void hideSignInLayout();

    void onLoginWithGoogleSuccessfull();
}

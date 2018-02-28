package com.mac.fireflies.wgt.createprofile.sign.view;

import com.mac.fireflies.wgt.createprofile.core.model.User;

/**
 * Created by Alestar on 2/11/2018.
 */

public interface SignUpFragmentView extends SignFragmentView {
    boolean isSignUpFormValid();

    boolean isConfirmAndFullNameValid();

    void onSingUpSuccessful(User user);
}

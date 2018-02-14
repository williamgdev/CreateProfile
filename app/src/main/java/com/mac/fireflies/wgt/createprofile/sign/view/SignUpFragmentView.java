package com.mac.fireflies.wgt.createprofile.sign.view;

import com.mac.fireflies.wgt.createprofile.core.model.W2TUser;

/**
 * Created by Alestar on 2/11/2018.
 */

public interface SignUpFragmentView extends SignFragmentView {
    boolean isSignUpFormValid();

    boolean isConfirmAndFullNameValid();

    void onSingUpSuccessful(W2TUser user);
}

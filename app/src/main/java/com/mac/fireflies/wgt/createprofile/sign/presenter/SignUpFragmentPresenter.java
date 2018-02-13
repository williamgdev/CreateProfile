package com.mac.fireflies.wgt.createprofile.sign.presenter;

import com.mac.fireflies.wgt.createprofile.BasePresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.view.SignUpFragmentView;

/**
 * Created by willimail on 2/12/18.
 */

public interface SignUpFragmentPresenter extends BasePresenter<SignUpFragmentView>, SignFragmentPresenter {
    void attemptSignUp();
}

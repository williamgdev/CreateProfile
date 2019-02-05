package com.mac.fireflies.wgt.createprofile.sign.view.state;

import android.widget.Button;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPhonePresenter;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.SignPhoneViewModel;

/**
 * Created by willimail on 2/25/18.
 */

public class VerifyCodePhoneViewState implements PhoneViewState {

    private final SignPhoneViewModel phoneViewModel;

    public VerifyCodePhoneViewState(TextView txtTitle, Button sendCodeButton, SignPhoneViewModel phoneViewModel) {
        this.phoneViewModel = phoneViewModel;
        setTitle(txtTitle, sendCodeButton);
    }

    @Override
    public void phoneCodeAction(String code) {
        phoneViewModel.verifyCode(code);
    }

    @Override
    public void setTitle(TextView title, Button button) {
        title.setText(R.string.enter_verification_code);
        button.setText(R.string.verify_code);
    }
}

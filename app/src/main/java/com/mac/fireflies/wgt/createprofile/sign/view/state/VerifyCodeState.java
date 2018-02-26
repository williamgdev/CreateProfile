package com.mac.fireflies.wgt.createprofile.sign.view.state;

import android.widget.Button;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPhonePresenter;

/**
 * Created by willimail on 2/25/18.
 */

public class VerifyCodeState implements State{

    private final SignPhonePresenter presenter;

    public VerifyCodeState(TextView txtTitle, Button sendCodeButton, SignPhonePresenter presenter) {
        this.presenter = presenter;
        setTitle(txtTitle, sendCodeButton);
    }

    @Override
    public void phoneCodeAction(String code) {
        presenter.verifyCode(code);
    }

    @Override
    public void setTitle(TextView title, Button button) {
        title.setText(R.string.enter_verification_code);
        button.setText(R.string.verify_code);
    }
}

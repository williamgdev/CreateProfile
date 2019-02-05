package com.mac.fireflies.wgt.createprofile.sign.view.state;

import android.widget.Button;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPhonePresenter;

/**
 * Created by willimail on 2/25/18.
 */


public class SendCodePhoneViewState implements PhoneViewState {

    private final SignPhonePresenter presenter;

    public SendCodePhoneViewState(TextView txtTitle, Button sendCodeButton) {
        //this.presenter = presenter;
        setTitle(txtTitle, sendCodeButton);
    }

    @Override
    public void phoneCodeAction(String phoneNumber) {
        presenter.sendVerificationCode(phoneNumber);
    }

    @Override
    public void setTitle(TextView title, Button button) {
        title.setText(R.string.enter_phone_number);
        button.setText(R.string.send_me_the_code);
    }
}

package com.mac.fireflies.wgt.createprofile.sign.view.state;

import android.widget.Button;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.SignPhoneViewModel;

/**
 * Created by willimail on 2/25/18.
 */


public class SendCodePhoneViewState implements PhoneViewState {

    private final SignPhoneViewModel phoneViewModel;

    public SendCodePhoneViewState(TextView txtTitle, Button sendCodeButton, SignPhoneViewModel phoneViewModel) {
        this.phoneViewModel = phoneViewModel;
        setTitle(txtTitle, sendCodeButton);
    }

    @Override
    public void phoneCodeAction(String phoneNumber) {
        phoneViewModel.getUserInput().postValue(phoneNumber);
    }

    @Override
    public void setTitle(TextView title, Button button) {
        title.setText(R.string.enter_phone_number);
        button.setText(R.string.send_me_the_code);
    }
}

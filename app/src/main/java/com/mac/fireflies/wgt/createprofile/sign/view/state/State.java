package com.mac.fireflies.wgt.createprofile.sign.view.state;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by willimail on 2/25/18.
 */

public interface State {

    void phoneCodeAction(String phoneNumber);

    void setTitle(TextView title, Button button);
}

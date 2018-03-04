package com.mac.fireflies.wgt.createprofile.sign.view;


import android.support.annotation.StringRes;
import android.widget.ArrayAdapter;

import com.mac.fireflies.wgt.createprofile.core.BaseView;
import com.mac.fireflies.wgt.createprofile.core.model.User;

public interface SignEmailFragmentView extends BaseView{

    boolean mayRequestContacts();

    void resetError();

    boolean isCredentialsValid();

    void setEmailAdapter(ArrayAdapter<String> adapter);

    void sendFieldsToPresenterFromState();

    boolean isSignFormValid();

    void setPasswordError(@StringRes int text);

    void setEmailError(@StringRes int text);

    void requestFocusView();

    void setConfirmationPasswordError(@StringRes int text);

    void setFirstNameError(@StringRes int text);

    void setLastNameError(@StringRes int text);

    void onSingUpSuccessful();

    void onLoginSuccessful();
}

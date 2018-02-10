package com.mac.fireflies.wgt.createprofile.view;

import android.widget.ArrayAdapter;

/**
 * Created by willimail on 2/10/18.
 */

public interface SignInFragmentView extends LoginView{
    boolean mayRequestContacts();

    void resetError();

    void updateCredentials();

    boolean isCredentialsValid();

    void setEmailAdapter(ArrayAdapter<String> adapter);

    void onLoginSuccessful(String emailLogged);
}

package com.mac.fireflies.wgt.createprofile.view;


import android.widget.ArrayAdapter;

/**
 * Created by willimail on 2/5/18.
 */

public interface LoginView extends BaseView {

    void signIn(String email, String password);

    boolean mayRequestContacts();

    void resetError();

    void sendCredentialsToPresenter();

    boolean isCredentialsValid();

    void setEmailAdapter(ArrayAdapter<String> adapter);
}

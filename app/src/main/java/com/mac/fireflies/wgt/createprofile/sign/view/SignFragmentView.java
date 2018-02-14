package com.mac.fireflies.wgt.createprofile.sign.view;

import android.widget.ArrayAdapter;

/**
 * Created by willimail on 2/12/18.
 */

public interface SignFragmentView  extends SignView {

    boolean mayRequestContacts();

    void resetError();

    boolean isCredentialsValid();

    void setEmailAdapter(ArrayAdapter<String> adapter);

    void sendFieldsToPresenter();
}

package com.mac.fireflies.wgt.createprofile.sign.view.state;


public interface SignEmailViewState {
    void sendFieldsToPresenter();

    boolean isSignFormValid();

    void attemptSignAction();
}

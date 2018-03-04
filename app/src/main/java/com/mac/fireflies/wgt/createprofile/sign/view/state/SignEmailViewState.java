package com.mac.fireflies.wgt.createprofile.sign.view.state;


public interface SignEmailViewState {
    void sendFields();

    boolean isSignFormValid();

    void attemptAction();
}

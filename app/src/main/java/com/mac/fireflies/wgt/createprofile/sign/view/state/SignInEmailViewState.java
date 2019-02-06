package com.mac.fireflies.wgt.createprofile.sign.view.state;


import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignEmailPresenter;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.SignEmailViewModel;

import java.util.HashMap;
import java.util.Map;

public class SignInEmailViewState implements SignEmailViewState {
    private final Button button;
    private AutoCompleteTextView txtEmail;
    private EditText txtPassword;
    //private SignEmailPresenter presenter;
    private SignEmailViewModel viewModel;

    public SignInEmailViewState(AutoCompleteTextView txtEmail, EditText txtPassword, Button mEmailSignButton, View signUpFields, SignEmailViewModel emailViewModel) {
        this.txtEmail = txtEmail;
        this.txtPassword = txtPassword;
        this.viewModel = emailViewModel;
        this.button = mEmailSignButton;
        this.button.setText(R.string.action_sign_in);
        signUpFields.setVisibility(View.GONE);
    }

    @Override
    public void sendFieldsToPresenter() {
        Map<String, String> fields = new HashMap<>();
        fields.put(SignEmailPresenter.FIELD_EMAIL, txtEmail.getText().toString());
        fields.put(SignEmailPresenter.FIELD_PASSWORD, txtPassword.getText().toString());
        viewModel.setFields(fields);
    }

    @Override
    public boolean isSignFormValid() {
        return viewModel.isCredentialsValid();
    }

    @Override
    public void attemptSignAction() {
        viewModel.attemptLogin();
    }
}

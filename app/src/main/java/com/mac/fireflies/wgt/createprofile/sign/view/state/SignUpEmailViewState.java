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

public class SignUpEmailViewState implements SignEmailViewState {
    private final Button button;
    private AutoCompleteTextView txtEmail;
    private EditText txtPassword;
    private final EditText txtConfirmPassword;
    private final EditText txtFirstName;
    private final EditText txtLastName;
    private final SignEmailViewModel viewModel;

    public SignUpEmailViewState(AutoCompleteTextView txtEmail, EditText txtPassword,
                                EditText txtConfirmPassword, EditText txtFirstName,
                                EditText txtLastName, Button mEmailSignButton,
                                View signUpFields, SignEmailViewModel emailViewModel) {
        this.txtEmail = txtEmail;
        this.txtPassword = txtPassword;
        this.txtConfirmPassword = txtConfirmPassword;
        this.txtFirstName = txtFirstName;
        this.txtLastName = txtLastName;
        this.viewModel = emailViewModel;
        this.button = mEmailSignButton;
        this.button.setText(R.string.action_sign_up);
        signUpFields.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendFieldsToPresenter() {
        Map<String, String> fields = new HashMap<>();
        fields.put(SignEmailPresenter.FIELD_EMAIL, txtEmail.getText().toString());
        fields.put(SignEmailPresenter.FIELD_PASSWORD, txtPassword.getText().toString());
        fields.put(SignEmailPresenter.FIELD_CONFIRM_PASSWORD, txtConfirmPassword.getText().toString());
        fields.put(SignEmailPresenter.FIELD_FIRST_NAME, txtFirstName.getText().toString());
        fields.put(SignEmailPresenter.FIELD_LAST_NAME, txtLastName.getText().toString());
        viewModel.setFields(fields);
    }

    @Override
    public boolean isSignFormValid() {
        return this.viewModel.isCredentialsValid() && viewModel.isConfirmAndFullNameValid();
    }

    @Override
    public void attemptSignAction() {
        viewModel.attemptSignUp();
    }
}

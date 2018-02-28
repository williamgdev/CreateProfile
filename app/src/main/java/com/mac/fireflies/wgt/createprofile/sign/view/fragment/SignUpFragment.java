package com.mac.fireflies.wgt.createprofile.sign.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignInFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignUpFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignUpFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.view.SignUpFragmentView;

public class SignUpFragment extends SignFragment implements SignUpFragmentView{
    SignUpFragmentPresenter presenter;

    private OnSignUpListener mListener;
    private TextView txtLastName;
    private TextView txtConfirmPassword;
    private TextView txtFirstName;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String email, String password) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(SignInFragmentPresenterImpl.EMAIL, email);
        args.putString(SignInFragmentPresenterImpl.PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {
        presenter = new SignUpFragmentPresenterImpl();
        if (getArguments() != null) {
            presenter.loadData(getArguments());
        }
        presenter.attachView(this);
        presenter.populateAutoComplete();
    }

    @Override
    protected void initializeUIComponents(View view) {
        txtConfirmPassword = (TextView) view.findViewById(R.id.password_confirmation);
        txtFirstName = (TextView) view.findViewById(R.id.fist_name);
        txtLastName = (TextView) view.findViewById(R.id.last_name);
        txtLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    presenter.attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignUpButton = (Button) view.findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.attemptSignUp();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected SignUpFragmentPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void sendFieldsToPresenter() {
        presenter.sendFields(txtEmail.getText().toString(),
                txtPassword.getText().toString(),
                txtConfirmPassword.getText().toString(),
                txtFirstName.getText().toString(),
                txtLastName.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpListener) {
            mListener = (OnSignUpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignUpListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean isSignUpFormValid() {
        return isCredentialsValid() && isConfirmAndFullNameValid();
    }

    @Override
    public boolean isConfirmAndFullNameValid() {
        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(txtConfirmPassword.getText().toString()) && !getPresenter().isConfirmationPasswordValid(txtConfirmPassword.getText().toString())) {
            txtConfirmPassword.setError(getString(R.string.error_invalid_confirmation_password));
            focusView = txtConfirmPassword;
            valid = false;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(txtFirstName.getText().toString())) {
            txtFirstName.setError(getString(R.string.error_field_required));
            focusView = txtFirstName;
            valid = false;
        }
        if (TextUtils.isEmpty(txtLastName.getText().toString())) {
            txtLastName.setError(getString(R.string.error_field_required));
            focusView = txtLastName;
            valid = false;
        }

        if (!valid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

        return valid;
    }

    @Override
    public void onSingUpSuccessful(User user) {
        mListener.onSignUpSuccessful(user);
    }

    public interface OnSignUpListener {
        void onSignUpSuccessful(User user);
    }
}

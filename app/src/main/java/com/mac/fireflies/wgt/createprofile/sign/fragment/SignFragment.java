package com.mac.fireflies.wgt.createprofile.sign.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.view.SignFragmentView;

import static android.Manifest.permission.READ_CONTACTS;

public abstract class SignFragment extends Fragment implements SignFragmentView {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    protected static final int REQUEST_READ_CONTACTS = 0;

    protected AutoCompleteTextView txtEmail;
    protected EditText txtPassword;

    protected View progressBar;
    protected View scrollView;

    public SignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutID(), container, false);

        // Set up the login form.
        txtEmail = (AutoCompleteTextView) view.findViewById(R.id.email);

        txtPassword = (EditText) view.findViewById(R.id.password);

        scrollView = view.findViewById(R.id.login_form);
        progressBar = view.findViewById(R.id.login_progress);
        initializeUIComponents(view);
        initializePresenter();

        return view;
    }

    protected abstract void initializePresenter();

    protected abstract void initializeUIComponents(View view);

    protected abstract int getLayoutID();

    protected abstract SignFragmentPresenter getPresenter();

    @Override
    public boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(txtEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void resetError() {
        // Reset errors.
        txtEmail.setError(null);
        txtPassword.setError(null);
    }

    @Override
    public void sendCredentialsToPresenter() {
        // Store values at the time of the login attempt.
        getPresenter().setCredentials(txtEmail.getText().toString(), txtPassword.getText().toString());
    }

    @Override
    public boolean isCredentialsValid() {
        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(txtPassword.getText().toString()) && !getPresenter().isPasswordValid(txtPassword.getText().toString())) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            valid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            valid = false;
        } else if (!getPresenter().isEmailValid(txtEmail.getText().toString())) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtEmail;
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
    public void setEmailAdapter(ArrayAdapter<String> adapter) {
        txtEmail.setAdapter(adapter);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPresenter().populateAutoComplete();
            }
        }
    }

    /**
     * Shows the progressBar UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progressBar spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            scrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            scrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void setInfo(String email) {
        // @TODO Implement StatusBar
    }

    @Override
    public void showText(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

}

package com.mac.fireflies.wgt.createprofile.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.SinInFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.presenter.SinInFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.view.SignInFragmentView;

import static android.Manifest.permission.READ_CONTACTS;

public class SignInFragment extends Fragment implements SignInFragmentView {
    SinInFragmentPresenter presenter;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private OnFragmentInteractionListener mListener;
    private Context context;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(String email, String password) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(SinInFragmentPresenterImpl.EMAIL, email);
        args.putString(SinInFragmentPresenterImpl.PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SinInFragmentPresenterImpl();
        if (getArguments() != null) {
            presenter.loadData(getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sig_in, container, false);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        presenter.attachView(this);
        presenter.populateAutoComplete();

        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    presenter.attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.attemptLogin();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    @Override
    public void updateCredentials() {
        // Store values at the time of the login attempt.
        presenter.setCredentials(mEmailView.getText().toString(), mPasswordView.getText().toString());
    }

    @Override
    public boolean isCredentialsValid() {
        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPasswordView.getText().toString()) && !presenter.isPasswordValid(mPasswordView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            valid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            valid = false;
        } else if (!presenter.isEmailValid(mEmailView.getText().toString())) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onLoginSuccessful(String emailLogged) {
        mListener.onLoginSuccessful(emailLogged);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.populateAutoComplete();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public void signIn(String email, String password) {
        presenter.signIn(email, password);
    }

    @Override
    public void setInfo(String email) {
        // @TODO Implement StatusBar
    }

    @Override
    public void showText(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onLoginSuccessful(String email);
    }
}

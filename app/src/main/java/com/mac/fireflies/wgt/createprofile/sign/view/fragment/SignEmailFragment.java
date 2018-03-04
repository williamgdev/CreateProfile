package com.mac.fireflies.wgt.createprofile.sign.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignEmailPresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignEmailPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.view.SignEmailFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SignEmailViewState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SignInEmailViewState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SignUpEmailViewState;

import static android.Manifest.permission.READ_CONTACTS;


public class SignEmailFragment extends Fragment implements SignEmailFragmentView {
    SignEmailViewState state;

    private OnSignWithEmailListener onSignWithEmailListener;
    private AutoCompleteTextView txtEmail;
    private EditText txtPassword;
    private EditText txtLastName;
    private EditText txtConfirmPassword;
    private EditText txtFirstName;
    private View scrollView;
    private View progressBar;
    private View focusView = null;

    protected static final int REQUEST_READ_CONTACTS = 0;
    private SignEmailPresenter presenter;
    private Button mEmailSignButton;
    private View signUpFields;

    public static SignEmailFragment newInstance(boolean isSignUp) {
        SignEmailFragment fragment = new SignEmailFragment();
        Bundle args = new Bundle();
        args.putBoolean(SignEmailPresenterImpl.IS_SIGN_UP, isSignUp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_email, container, false);
        txtEmail = view.findViewById(R.id.email);
        txtPassword = view.findViewById(R.id.password);
        txtConfirmPassword = view.findViewById(R.id.password_confirmation);
        txtFirstName = view.findViewById(R.id.fist_name);
        txtLastName = view.findViewById(R.id.last_name);
        mEmailSignButton = view.findViewById(R.id.email_sign_button);
        signUpFields = view.findViewById(R.id.sign_up_fields);
        progressBar = view.findViewById(R.id.progressBar);
        presenter = new SignEmailPresenterImpl();
        presenter.attachView(this);
        presenter.loadData(getArguments());
        if (presenter.isSignUp()) {
            state = new SignUpEmailViewState(txtEmail, txtPassword, txtConfirmPassword, txtFirstName, txtLastName, mEmailSignButton, signUpFields, presenter);
            txtLastName.setOnEditorActionListener(editorActionListener);
        } else {
            state = new SignInEmailViewState(txtEmail, txtPassword, mEmailSignButton, signUpFields, presenter);
            txtPassword.setOnEditorActionListener(editorActionListener);
        }
        mEmailSignButton.setOnClickListener(onClickListener);
        return view;
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                state.attemptSignAction();
                return true;
            }
            return false;
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            state.attemptSignAction();
        }
    };

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        getView().findViewById(R.id.container_layout).setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getView().findViewById(R.id.container_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void setInfo(String email) {
        showText(email);
    }

    @Override
    public void showText(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

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
    public boolean isCredentialsValid() {
        return state.isSignFormValid();
    }

    @Override
    public void setEmailAdapter(ArrayAdapter<String> adapter) {
        txtEmail.setAdapter(adapter);
    }

    @Override
    public void sendFieldsToPresenterFromState() {
        state.sendFieldsToPresenter();
    }

    @Override
    public boolean isSignFormValid() {
        return state.isSignFormValid();
    }

    @Override
    public void setPasswordError(@StringRes int text) {
        setErrorAndFocus(txtPassword, text);
    }

    @Override
    public void setEmailError(int text) {
        setErrorAndFocus(txtEmail, text);
    }

    @Override
    public void setConfirmationPasswordError(int text) {
        setErrorAndFocus(txtConfirmPassword, text);
    }

    @Override
    public void setFirstNameError(int text) {
        setErrorAndFocus(txtFirstName, text);
    }

    @Override
    public void setLastNameError(int text) {
        setErrorAndFocus(txtLastName, text);
    }

    private void setErrorAndFocus(EditText editText, @StringRes int text) {
        editText.setError(getString(text));
        focusView = editText;
    }

    @Override
    public void requestFocusView() {
        focusView.requestFocus();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignWithEmailListener) {
            onSignWithEmailListener = (OnSignWithEmailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignUpListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSignWithEmailListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSingUpSuccessful() {
        onSignWithEmailListener.onSignUpSuccessful();
    }

    @Override
    public void onLoginSuccessful() {
        onSignWithEmailListener.onSignInSuccessful();
    }

    public interface OnSignWithEmailListener {
        void onSignInSuccessful();
        void onSignUpSuccessful();
    }
}

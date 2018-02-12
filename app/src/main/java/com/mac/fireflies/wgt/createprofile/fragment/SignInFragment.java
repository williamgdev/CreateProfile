package com.mac.fireflies.wgt.createprofile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.SinInFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.presenter.SinInFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.view.SignInFragmentView;

public class SignInFragment extends SignBaseFragment implements SignInFragmentView {
    SinInFragmentPresenter presenter;

    private OnFragmentInteractionListener mListener;

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
    protected void initializePresenter() {
        presenter.attachView(this);
        presenter.populateAutoComplete();
    }

    @Override
    protected void initializeUIComponents(View view) {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);

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
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sig_in;
    }

    @Override
    protected SinInFragmentPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public void onLoginSuccessful(String emailLogged) {
        mListener.onLoginSuccessful(emailLogged);
    }


    @Override
    public void signIn(String email, String password) {
        presenter.signIn(email, password);
    }

    public interface OnFragmentInteractionListener {
        void onLoginSuccessful(String email);
    }
}

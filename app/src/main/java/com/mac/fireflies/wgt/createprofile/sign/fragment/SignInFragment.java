package com.mac.fireflies.wgt.createprofile.sign.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignInFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignInFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.view.SignInFragmentView;

public class SignInFragment extends SignFragment implements SignInFragmentView {
    SignInFragmentPresenter presenter;

    private OnFragmentInteractionListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(String email, String password) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(SignInFragmentPresenterImpl.EMAIL, email);
        args.putString(SignInFragmentPresenterImpl.PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initializePresenter() {
        presenter = new SignInFragmentPresenterImpl();
        if (getArguments() != null) {
            presenter.loadData(getArguments());
        }
        presenter.attachView(this);
        presenter.populateAutoComplete();
    }

    @Override
    protected void initializeUIComponents(View view) {
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        TextView signUpLink = (TextView) view.findViewById(R.id.sign_up_link);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSignUpClicked();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sign_in;
    }

    @Override
    protected SignFragmentPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public void onLoginSuccessful(String emailLogged) {
        mListener.onLoginSuccessful(emailLogged);
    }


    @Override
    public void signIn(String email, String password) {
        presenter.signIn(email, password);
    }

    public interface OnFragmentInteractionListener {
        void onLoginSuccessful(String email);
        void onSignUpClicked();
    }
}

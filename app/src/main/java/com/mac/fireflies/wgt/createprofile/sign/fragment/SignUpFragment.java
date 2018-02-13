package com.mac.fireflies.wgt.createprofile.sign.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignInFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignUpFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignUpFragmentPresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.view.SignUpFragmentView;

public class SignUpFragment extends SignFragment implements SignUpFragmentView{
    SignUpFragmentPresenter presenter;

    private OnSignUpListener mListener;

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
//        txtConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                    presenter.attemptSignUp();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                presenter.attemptSignUp();
//            }
//        });
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

    public interface OnSignUpListener {
        void onFragmentInteraction(Uri uri);
    }
}

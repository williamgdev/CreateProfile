package com.mac.fireflies.wgt.createprofile.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.presenter.SignInFragmentPresenter;
import com.mac.fireflies.wgt.createprofile.presenter.SignInFragmentPresenterImpl;

public class SignUpFragment extends SignBaseFragment {
    SignInFragmentPresenter presenter;

    private OnSingUpListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sing_up, container, false);
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

    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected SignInFragmentPresenter getPresenter() {
        return null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSingUpListener) {
            mListener = (OnSingUpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSingUpListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void signIn(String email, String password) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSingUpListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

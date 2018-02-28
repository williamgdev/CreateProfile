package com.mac.fireflies.wgt.createprofile.sign.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SendCodeState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.State;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPhonePresenter;
import com.mac.fireflies.wgt.createprofile.sign.presenter.SignPhonePresenterImpl;
import com.mac.fireflies.wgt.createprofile.sign.view.SignPhoneFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.state.VerifyCodeState;

public class SignPhoneFragment extends Fragment implements SignPhoneFragmentView {
    SignPhonePresenter presenter;
    State state;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSignPhoneFragmentListener mListener;
    private EditText txtPhoneNumber;
    private TextView txtTitle;
    private Button sendCodeButton;

    public SignPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignPhoneFragment newInstance(String param1, String param2) {
        SignPhoneFragment fragment = new SignPhoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_phone, container, false);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtTitle = view.findViewById(R.id.txt_title);
        sendCodeButton = view.findViewById(R.id.button_send_code);
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCodeAction();
            }
        });
        state = new SendCodeState(txtTitle, sendCodeButton, presenter);
        return view;
    }

    @Override
    public void phoneCodeAction() {
        state.phoneCodeAction(txtPhoneNumber.getText().toString());
        txtPhoneNumber.setText("");
        switch (state.getClass().getSimpleName()) {
            case "SendCodeState":
                state = new VerifyCodeState(txtTitle, sendCodeButton, presenter);
                break;

            case "VerifyCodeState":
                state = null;
                break;
        }

    }

    @Override
    public void onLoginSuccessful(User result) {
        mListener.onPhoneLoginSuccessful(result);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new SignPhonePresenterImpl();
        presenter.attachView(this);
        if (context instanceof OnSignPhoneFragmentListener) {
            mListener = (OnSignPhoneFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignPhoneFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        presenter.detachView();
    }

    @Override
    public void showProgress(boolean b) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setInfo(String email) {
    }

    @Override
    public void showText(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        state = new VerifyCodeState(txtTitle, sendCodeButton, presenter);
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
    public interface OnSignPhoneFragmentListener {
        // TODO: Update argument type and name
        void onPhoneLoginSuccessful(User uri);
    }

}
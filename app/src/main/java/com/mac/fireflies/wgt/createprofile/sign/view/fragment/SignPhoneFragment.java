package com.mac.fireflies.wgt.createprofile.sign.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.databinding.FragmentSignPhoneBinding;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SendCodePhoneViewState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.PhoneViewState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.VerifyCodePhoneViewState;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.SignNavigator;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.SignPhoneViewModel;

import java.lang.ref.WeakReference;

public class SignPhoneFragment extends Fragment implements SignNavigator {
    PhoneViewState phoneViewState;
    SignPhoneViewModel viewModel;
    private AppCoreInteractor appCoreInteractor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSignPhoneFragmentListener mListener;
    private FragmentSignPhoneBinding binding;

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

        viewModel = ViewModelProviders.of(this).get(SignPhoneViewModel.class);
        viewModel.signNavigator = new WeakReference(this);
        appCoreInteractor = AppCoreInteractor.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSignPhoneBinding.inflate(inflater, container, false);
        binding.setViewmodel(viewModel);
        phoneViewState = new SendCodePhoneViewState(binding.txtTitle, binding.buttonSendCode, viewModel);
        viewModel.getSendPhone().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sendPhone) {
                if (sendPhone != null) {
                    phoneCodeAction();
                }
            }
        });
        return binding.getRoot();
    }

    private AppCoreInteractor.AppCoreListener<User> signPhoneListener = new AppCoreInteractor.AppCoreListener<User>() {
        @Override
        public void onResult(User result) {
            onLoginSuccessful(result);
            hideProgress();
        }

        @Override
        public void onError(String error) {
            showText(error);
            hideProgress();
        }
    };

    public void verifyCode(String code) {
        appCoreInteractor.verifyPhoneCode(code, signPhoneListener);
    }

    public void sendVerificationCode(String phoneNumber) {
        this.showProgress();
        appCoreInteractor.signInWithPhone(phoneNumber, this.getActivity(), signPhoneListener, new AppCoreInteractor.SentCodeListener() {
            @Override
            public void onCodeSent() {
                hideProgress();
            }
        });
    }

    public void phoneCodeAction() {
        phoneViewState.phoneCodeAction(viewModel.getUserInput().getValue());
        viewModel.getUserInput().postValue("");
        switch (phoneViewState.getClass().getSimpleName()) {
            case "SendCodePhoneViewState":
                phoneViewState = new VerifyCodePhoneViewState(binding.txtTitle, binding.buttonSendCode, viewModel);
                break;

            case "VerifyCodePhoneViewState":
                phoneViewState = null;
                break;
        }
    }

    public void onLoginSuccessful(User result) {
        mListener.onPhoneLoginSuccessful(result);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    }

    public void showProgress() {

    }

    public void hideProgress() {

    }

    public void setInfo(String email) {
    }

    public void showText(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        phoneViewState = new VerifyCodePhoneViewState(binding.txtTitle, binding.buttonSendCode, viewModel);
    }

    public void gotoSignINWithPhone() {
        sendVerificationCode(viewModel.getUserInput().getValue());
    }

    @Override
    public void gotoVerifyCode() {
        verifyCode(viewModel.getUserInput().getValue());
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
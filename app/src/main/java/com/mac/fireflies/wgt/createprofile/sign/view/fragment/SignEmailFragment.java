package com.mac.fireflies.wgt.createprofile.sign.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.databinding.FragmentSignEmailBinding;
import com.mac.fireflies.wgt.createprofile.sign.view.SignEmailFragmentView;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SignEmailViewState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SignInEmailViewState;
import com.mac.fireflies.wgt.createprofile.sign.view.state.SignUpEmailViewState;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.ISignEmailViewModel;
import com.mac.fireflies.wgt.createprofile.sign.viewmodel.SignEmailViewModel;

import org.jetbrains.annotations.NotNull;

import static android.Manifest.permission.READ_CONTACTS;


public class SignEmailFragment extends Fragment implements SignEmailFragmentView, ISignEmailViewModel {
    private SignEmailViewState state;
    private SignEmailViewModel viewModel;
    private FragmentSignEmailBinding binding;
    private AppCoreInteractor appCoreInteractor;

    private OnSignWithEmailListener onSignWithEmailListener;
    private View scrollView;
    private View progressBar;
    private View focusView = null;

    protected static final int REQUEST_READ_CONTACTS = 0;

    public static SignEmailFragment newInstance(boolean isSignUp) {
        SignEmailFragment fragment = new SignEmailFragment();
        SignEmailViewModel viewModel = ViewModelProviders.of(fragment).get(SignEmailViewModel.class);
        Bundle args = new Bundle();
        args.putBoolean(viewModel.IS_SIGN_UP, isSignUp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appCoreInteractor = AppCoreInteractor.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignEmailBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(SignEmailViewModel.class);

        progressBar = binding.getRoot().findViewById(R.id.progressBar);
        viewModel.loadData(getArguments());
        if (viewModel.isSignUp()) {
            state = new SignUpEmailViewState(
                    binding.email,
                    binding.password,
                    binding.passwordConfirmation,
                    binding.fistName,
                    binding.lastName,
                    binding.emailSignButton,
                    binding.signUpFields,
                    viewModel);
            binding.lastName.setOnEditorActionListener(editorActionListener);
        } else {
            state = new SignInEmailViewState(
                    binding.email,
                    binding.password,
                    binding.emailSignButton,
                    binding.signUpFields,
                    viewModel);
            binding.password.setOnEditorActionListener(editorActionListener);
        }
        binding.emailSignButton.setOnClickListener(onClickListener);
        return binding.getRoot();
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
            Toast.makeText(getContext(),binding.email + getContext()
                            .getResources()
                            .getString(R.string.permission_rationale),
                    Toast.LENGTH_LONG);
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void resetError() {
        // Reset errors.
        binding.email.setError(null);
        binding.password.setError(null);
    }

    @Override
    public boolean isCredentialsValid() {
        return state.isSignFormValid();
    }

    @Override
    public void setEmailAdapter(ArrayAdapter<String> adapter) {
        binding.email.setAdapter(adapter);
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
        setErrorAndFocus(binding.password, text);
    }

    @Override
    public void setEmailError(int text) {
        setErrorAndFocus(binding.email, text);
    }

    @Override
    public void setConfirmationPasswordError(int text) {
        setErrorAndFocus(binding.passwordConfirmation, text);
    }

    @Override
    public void setFirstNameError(int text) {
        setErrorAndFocus(binding.fistName, text);
    }

    @Override
    public void setLastNameError(int text) {
        setErrorAndFocus(binding.lastName, text);
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
                //presenter.populateAutoComplete();
                mayRequestContacts();
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
        //presenter.detachView();
    }

    @Override
    public void onSingUpSuccessful() {
        onSignWithEmailListener.onSignUpSuccessful();
    }

    @Override
    public void onLoginSuccessful() {
        onSignWithEmailListener.onSignInSuccessful();
    }

    @Override
    public void callRequestFocusView() {
        requestFocusView();
    }

    @Override
    public void callSetInvalidPasswordError() {
        setPasswordError(R.string.error_invalid_password);
    }

    @Override
    public void callSetRequiredPasswordError() {
        setPasswordError(R.string.error_field_required);
    }

    @Override
    public void callSetEmailError() {
        setEmailError(R.string.error_invalid_email);
    }

    @Override
    public void callSignIn(@NotNull String email, @NotNull String password) {
        appCoreInteractor.signIn(email, password, new AppCoreInteractor.AppCoreListener<User>() {
            @Override
            public void onResult(User result) {
                hideProgress();
                onLoginSuccessful();
            }

            @Override
            public void onError(String error) {
                hideProgress();
            }
        });
    }

    @Override
    public void callResetErrors() {
        resetError();
    }

    @Override
    public void callShowProgress() {
        showProgress();
    }

    @Override
    public void callSignUp(@NotNull String email, @NotNull String password) {
        showProgress();
        appCoreInteractor.signUp(email, password, new AppCoreInteractor.AppCoreListener<User>() {
            @Override
            public void onResult(User result) {
                hideProgress();
                onSingUpSuccessful();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                showText(error);
            }
        });
    }

    @Override
    public void callSendFieldsToPresenterFromState() {
        sendFieldsToPresenterFromState();
    }

    @Override
    public boolean callIsSignFormValid() {
        return isSignFormValid();
    }

    @Override
    public void callSetConfirmationPasswordError() {
        setConfirmationPasswordError(R.string.error_invalid_confirmation_password);
    }

    @Override
    public void callSetFirstNameError() {
        setFirstNameError(R.string.error_field_required);
    }

    @Override
    public void callSetLastNameError() {
        setLastNameError(R.string.error_field_required);
    }

    public interface OnSignWithEmailListener {
        void onSignInSuccessful();

        void onSignUpSuccessful();
    }
}

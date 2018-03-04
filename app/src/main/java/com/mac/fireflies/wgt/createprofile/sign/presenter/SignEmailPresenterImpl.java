package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.mac.fireflies.wgt.createprofile.R;
import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor;
import com.mac.fireflies.wgt.createprofile.core.model.User;
import com.mac.fireflies.wgt.createprofile.sign.view.SignEmailFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by willimail on 2/12/18.
 */

public class SignEmailPresenterImpl implements SignEmailPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String IS_SIGN_UP = "IS_SIGN_UP";

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;
    private String mConfirmationPassword;

    private SignEmailFragmentView view;
    private AppCoreInteractor appCoreInteractor;
    private boolean isSignUp;

    @Override
    public boolean isSignUp() {
        return isSignUp;
    }

    @Override
    public void loadData(Bundle arguments) {
        isSignUp = arguments.getBoolean(SignEmailPresenterImpl.IS_SIGN_UP);
    }

    @Override
    public void populateAutoComplete() {
        if (!view.mayRequestContacts()) {
            return;
        }

        view.getActivity().getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(view.getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(view.getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        view.setEmailAdapter(adapter);
    }

    @Override
    public void attemptSignUp() {
        view.resetError();

        view.sendFieldsToPresenterFromState();

        if (view.isSignFormValid()) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            view.showProgress();
            signUp();
        }
    }

    @Override
    public void attemptLogin() {
        view.resetError();

        view.sendFieldsToPresenterFromState();

        if(view.isCredentialsValid()) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            view.showProgress();
            signIn(mEmail, mPassword);
        }
    }

    @Override
    public boolean isCredentialsValid() {
        boolean valid = true;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPassword) && !isPasswordValid(mPassword)) {
            view.setPasswordError(R.string.error_invalid_password);
            valid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mPassword)) {
            view.setPasswordError(R.string.error_field_required);
            valid = false;
        } else if (!isEmailValid(mEmail)) {
            view.setEmailError(R.string.error_invalid_email);
            valid = false;
        }

        if (!valid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            view.requestFocusView();
        }
        return valid;
    }


    @Override
    public boolean isConfirmAndFullNameValid() {
        boolean valid = true;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPassword) && !isConfirmationPasswordValid(mConfirmationPassword)) {
            view.setConfirmationPasswordError(R.string.error_invalid_confirmation_password);
            valid = false;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(mFirstName)) {
            view.setFirstNameError(R.string.error_field_required);
            valid = false;
        }
        if (TextUtils.isEmpty(mLastName)) {
            view.setLastNameError(R.string.error_field_required);
            valid = false;
        }

        if (!valid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            view.requestFocusView();
        }

        return valid;
    }

    @Override
    public void setFields(Map<String, String> fields) {
        mEmail = fields.get(SignEmailPresenter.FIELD_EMAIL);
        mPassword = fields.get(SignEmailPresenter.FIELD_PASSWORD);
        if (isSignUp()) {
            mConfirmationPassword = fields.get(SignEmailPresenter.FIELD_CONFIRM_PASSWORD);
            mFirstName = fields.get(SignEmailPresenter.FIELD_FIRST_NAME);
            mLastName = fields.get(SignEmailPresenter.FIELD_LAST_NAME);
        }
    }

    @Override
    public boolean isConfirmationPasswordValid(String confirmationPassword) {
        return mPassword.equals(confirmationPassword);
    }

    private void signUp() {
        view.showProgress();
        appCoreInteractor.signUp(mEmail, mPassword, new AppCoreInteractor.AppCoreListener<User>() {
            @Override
            public void onResult(User result) {
                view.hideProgress();
                view.onSingUpSuccessful();
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
                view.showText(error);
            }
        });
    }

    @Override
    public void signIn(String email, String password) {
        appCoreInteractor.signIn(email, password, new AppCoreInteractor.AppCoreListener<User>() {
            @Override
            public void onResult(User result) {
                view.hideProgress();
                view.onLoginSuccessful();
            }

            @Override
            public void onError(String error) {
                view.hideProgress();
            }
        });
    }

    @Override
    public void attachView(SignEmailFragmentView view) {
        this.view = view;
        appCoreInteractor = AppCoreInteractor.getInstance();
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadData(Intent data) {

    }

    @Override
    public void displayLoggedUser() {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

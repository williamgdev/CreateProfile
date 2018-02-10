package com.mac.fireflies.wgt.createprofile.presenter;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import com.mac.fireflies.wgt.createprofile.interactor.FirebaseInteractor;
import com.mac.fireflies.wgt.createprofile.model.W2TUser;
import com.mac.fireflies.wgt.createprofile.view.SignInFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willimail on 2/10/18.
 */

public class SinInFragmentPresenterImpl implements LoaderManager.LoaderCallbacks<Cursor>, SinInFragmentPresenter {
    private SignInFragmentView view;

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private String mEmail;
    private String mPassword;

    private FirebaseInteractor firebaseInteractor;

    @Override
    public void attachView(SignInFragmentView view) {
        this.view = view;
        firebaseInteractor = FirebaseInteractor.getInstance();
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void loadData(Intent data) {

    }

    @Override
    public void getCurrentUser() {

    }

    @Override
    public void loadData(Bundle arguments) {
        mEmail = arguments.getString(SinInFragmentPresenterImpl.EMAIL);
        mPassword = arguments.getString(SinInFragmentPresenterImpl.PASSWORD);
    }

    @Override
    public void populateAutoComplete() {
        if (!view.mayRequestContacts()) {
            return;
        }

        view.getActivity().getLoaderManager().initLoader(0, null, this);
    }

    @Override
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        view.resetError();

        view.updateCredentials();

        if(view.isCredentialsValid()) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            view.showProgress(true);
            signIn(mEmail, mPassword);
        }
    }

    @Override
    public void setCredentials(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    public void signIn(String email, String password) {
        firebaseInteractor.signIn(email, password, new FirebaseInteractor.FirebaseListener<W2TUser>() {
            @Override
            public void onResult(W2TUser result) {
                view.showProgress(false);
                view.onLoginSuccessful(result.getEmail());
            }

            @Override
            public void onError(String error) {
                view.showProgress(false);
            }
        });
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

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
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

}

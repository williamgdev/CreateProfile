package com.mac.fireflies.wgt.createprofile.sign.presenter;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import com.mac.fireflies.wgt.createprofile.sign.view.SignFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willimail on 2/12/18.
 */

public abstract class SignFragmentPresenterAbst implements SignFragmentPresenter, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    protected String mEmail;
    protected String mPassword;

    @Override
    public void loadData(Bundle arguments) {
        mEmail = arguments.getString(SignInFragmentPresenterImpl.EMAIL);
        mPassword = arguments.getString(SignInFragmentPresenterImpl.PASSWORD);
    }

    @Override
    public void populateAutoComplete() {
        if (!getView().mayRequestContacts()) {
            return;
        }

        getView().getActivity().getLoaderManager().initLoader(0, null, this);
    }

    protected abstract SignFragmentView getView();

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
        return new CursorLoader(getView().getActivity(),
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
                new ArrayAdapter<>(getView().getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        getView().setEmailAdapter(adapter);
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

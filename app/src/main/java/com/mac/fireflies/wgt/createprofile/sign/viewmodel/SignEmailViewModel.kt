package com.mac.fireflies.wgt.createprofile.sign.viewmodel

import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class SignEmailViewModel : ViewModel() {
    private var mIsSignUp : Boolean = false
    public val IS_SIGN_UP = "IS_SIGN_UP"

    val FIELD_EMAIL = "EMAIL"
    val FIELD_PASSWORD = "PASSWORD"
    val FIELD_CONFIRM_PASSWORD = "CONFIRM_PASSWORD"
    val FIELD_FIRST_NAME = "FIRST_NAME"
    val FIELD_LAST_NAME = "LAST_NAME"

    private var mFirstName: String? = null
    private var mLastName: String? = null
    private var mEmail: String? = null
    private var mPassword: String? = null
    private var mConfirmationPassword: String? = null
    lateinit var actions : WeakReference<ISignEmailViewModel>

    fun isSignUp(): Boolean {
        return mIsSignUp
    }
    fun loadData(arguments: Bundle) {
        mIsSignUp = arguments.getBoolean(IS_SIGN_UP)
    }

    fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    fun isConfirmationPasswordValid(confirmationPassword: String): Boolean {
        return mPassword == confirmationPassword
    }

    fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }
            fun isCredentialsValid(): Boolean
            {
        var valid = true
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPassword) && !isPasswordValid(mPassword!!)) {
            actions.get()?.callSetInvalidPasswordError()
            valid = false
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mPassword)) {
            actions.get()?.callSetRequiredPasswordError()
            valid = false
        } else if (!isEmailValid(mEmail!!)) {
            actions.get()?.callSetEmailError()
            valid = false
        }

        if (!valid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            actions.get()?.callRequestFocusView()
        }

        return valid
    }

    fun isConfirmAndFullNameValid(): Boolean {
        var valid = true

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPassword) && !isConfirmationPasswordValid(mConfirmationPassword!!)) {
            actions.get()?.callSetConfirmationPasswordError()
            valid = false
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(mFirstName)) {
            actions.get()?.callSetFirstNameError()
            valid = false
        }
        if (TextUtils.isEmpty(mLastName)) {
            actions.get()?.callSetLastNameError()
            valid = false
        }

        if (!valid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            actions.get()?.callRequestFocusView()
        }

        return valid
    }
    fun setFields(fields: Map<String, String>) {
        mEmail = fields[FIELD_EMAIL]
        mPassword = fields[FIELD_PASSWORD]
        if (isSignUp()) {
            mConfirmationPassword = fields[FIELD_CONFIRM_PASSWORD]
            mFirstName = fields[FIELD_FIRST_NAME]
            mLastName = fields[FIELD_LAST_NAME]
        }
    }

    fun attemptLogin() {
        actions.get()?.callResetErrors()

        actions.get()?.callSendFieldsToPresenterFromState()

        if (isCredentialsValid()) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            actions.get()?.callShowProgress()
            actions.get()?.callSignIn(mEmail!!, mPassword!!)
        }
    }

    fun attemptSignUp(){
        actions.get()?.callResetErrors()

        actions.get()?.callSendFieldsToPresenterFromState()

        var isSignFormValid = actions.get()?.callIsSignFormValid()
        if (isSignFormValid!!) {
            // Show a progressBar spinner, and kick off a background task to
            // perform the user login attempt.
            actions.get()?.callShowProgress()
            actions.get()?.callSignUp(mEmail!!, mPassword!!)
        }
    }
}
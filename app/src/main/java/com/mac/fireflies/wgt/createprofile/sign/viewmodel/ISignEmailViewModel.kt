package com.mac.fireflies.wgt.createprofile.sign.viewmodel

interface ISignEmailViewModel {
    fun callSetInvalidPasswordError()
    fun callSetRequiredPasswordError()
    fun callSetEmailError()
    fun callSetConfirmationPasswordError()
    fun callSetFirstNameError()
    fun callSetLastNameError()
    fun callRequestFocusView()
    fun callSignIn(email: String, password: String)
    fun callSignUp(email: String, password: String)
    fun callResetErrors()
    fun callShowProgress()
    fun callSendFieldsToPresenterFromState()
    fun callIsSignFormValid() : Boolean
}
package com.mac.fireflies.wgt.createprofile.sign.viewmodel

import androidx.lifecycle.ViewModel
import com.mac.fireflies.wgt.createprofile.core.interactor.AppCoreInteractor

class SignPhoneViewModel : ViewModel() {
    private lateinit var code : String
    private lateinit var phoneNumber : String

    fun sendVerificationCode(phoneNumber: String) {
        //view.showProgress()
       // appCoreInteractor.signInWithPhone(phoneNumber, view.getActivity(), signPhoneListener, AppCoreInteractor.SentCodeListener { view.hideProgress() })
    }

    fun verifyCode(code: String) {
        //appCoreInteractor.verifyPhoneCode(code, signPhoneListener)
    }

    fun getPhoneNumber(): String
    {
        return phoneNumber
    }
    fun getCode(): String
    {
        return code
    }
}
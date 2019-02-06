package com.mac.fireflies.wgt.createprofile.sign.viewmodel

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class SignPhoneViewModel : ViewModel() {
    private lateinit var code: String
    private lateinit var phoneNumber: String
    lateinit var signNavigator: WeakReference<SignNavigator>

    fun sendVerificationCode(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        signNavigator.get()?.gotoSignINWithPhone()
    }

    fun verifyCode(code: String) {
        this.code = code
        signNavigator.get()?.gotoVerifyCode()
    }

    fun getPhoneNumber(): String {
        return phoneNumber
    }

    fun getCode(): String {
        return code
    }
    fun phoneCodeAction(){
        signNavigator.get()?.callPhoneCodeAction()
    }
}

interface SignNavigator {
    fun gotoSignINWithPhone()
    fun gotoVerifyCode()
    fun callPhoneCodeAction()
}

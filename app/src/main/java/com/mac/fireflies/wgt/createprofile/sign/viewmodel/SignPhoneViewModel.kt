package com.mac.fireflies.wgt.createprofile.sign.viewmodel

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class SignPhoneViewModel : ViewModel() {
    private lateinit var code : String
    private lateinit var phoneNumber : String
    private lateinit var signNavigator: WeakReference<SignNavigator>

    fun sendVerificationCode(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        signNavigator.get()?.gotoSignINWithPhone()
    }

    fun verifyCode(code: String) {
        signNavigator.get()?.gotoVerifyCode()
    }

    fun getPhoneNumber(): String
    {
        return phoneNumber
    }
    fun getCode(): String
    {
        this.code = code
        return code
    }
}

interface SignNavigator {
    fun gotoSignINWithPhone()
    fun gotoVerifyCode()
}

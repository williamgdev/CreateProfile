package com.mac.fireflies.wgt.createprofile.sign.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class SignPhoneViewModel : ViewModel() {
    lateinit var signNavigator: WeakReference<SignNavigator>
    var userInput = MutableLiveData<String>()
    var shouldSendPhone = MutableLiveData<Boolean?>().apply { postValue(null) }
    var phoneDataModel = PhoneData()

    fun phoneCodeAction(){
        when {
            shouldSendPhone.value == null -> {
                phoneDataModel.phoneNumber = userInput.value
                shouldSendPhone.postValue(true)
            }
            shouldSendPhone.value == true -> {
                phoneDataModel.code = userInput.value
                shouldSendPhone.postValue(false)
            }
        }
    }

    fun verifyCode() {
        signNavigator.get()?.gotoVerifyCode()
    }
}
data class PhoneData(var phoneNumber: String? = null, var code:String? = null)

interface SignNavigator {
    fun gotoVerifyCode()
}

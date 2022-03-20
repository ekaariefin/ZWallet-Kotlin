package com.ariefin.zwallet.ui.layout.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.CreatePinRequest
import com.ariefin.zwallet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {

    fun login(email: String, password: String): LiveData<Resource<APIResponse<User>?>> {
        return dataSource.login(email, password)
    }

    fun register(username: String, email: String, password: String): LiveData<Resource<APIResponse<User>?>> {
        return dataSource.register(username, email, password)
    }

    fun checkPIN(pin: String): LiveData<Resource<APIResponse<String>?>> {
        return dataSource.checkPIN(pin)
    }

    fun createPIN(pin: CreatePinRequest): LiveData<Resource<APIResponse<User>?>> {
        return  dataSource.createPIN(pin)
    }

}
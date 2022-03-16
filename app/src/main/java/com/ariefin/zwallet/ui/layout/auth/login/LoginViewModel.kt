package com.ariefin.zwallet.ui.layout.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {
    fun login(email: String, password: String): LiveData<Resource<APIResponse<User>?>> {
        return dataSource.login(email, password)
    }
}
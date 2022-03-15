package com.ariefin.zwallet.ui.auth.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.Resource

class LoginViewModel( app: Application): ViewModel() {
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    fun login(email: String, password: String): LiveData<Resource<APIResponse<User>?>> {
        return dataSource.login(email, password)
    }
}
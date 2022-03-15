package com.ariefin.zwallet.ui.main.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.Resource

class PersonalInfoViewModel(app: Application): ViewModel() {
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    fun getProfileInfo(): LiveData<Resource<APIResponse<UserDetail>?>> {
        return dataSource.getProfileInfo()
    }


}
package com.ariefin.zwallet.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.Invoice
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.network.NetworkConfig

class HomeViewModel(app: Application): ViewModel() {
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()
    private var dataSource = ZWalletDataSource(apiClient)

    fun getInvoice(): LiveData<APIResponse<List<Invoice>>> {
        return dataSource.getInvoice()
    }

    fun getBalance(): LiveData<APIResponse<List<UserDetail>>> {
        return dataSource.getBalance()
    }

}
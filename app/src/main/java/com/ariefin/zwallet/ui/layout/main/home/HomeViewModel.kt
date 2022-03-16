package com.ariefin.zwallet.ui.layout.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.Invoice
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {

    fun getInvoice(): LiveData<Resource<APIResponse<List<Invoice>>?>> {
        return dataSource.getInvoice()
    }

    fun getBalance(): LiveData<Resource<APIResponse<List<UserDetail>>?>> {
        return dataSource.getBalance()
    }

}
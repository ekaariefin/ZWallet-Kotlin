package com.ariefin.zwallet.ui.layout

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.Contact
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {
    fun getContact(): LiveData<Resource<APIResponse<List<Contact>>?>> {
        return dataSource.getContact()
    }

}
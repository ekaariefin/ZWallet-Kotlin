package com.ariefin.zwallet.ui.layout.main.transfer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariefin.zwallet.data.ZWalletDataSource
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.*
import com.ariefin.zwallet.model.request.TransferRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(private var dataSource: ZWalletDataSource): ViewModel() {
    private var selectedContact = MutableLiveData<Contact>()
    var data_amount  = MutableLiveData<Int>()
    var data_receiver = MutableLiveData<Int>()
    var data_notes = MutableLiveData<String>()

    private var transfer = MutableLiveData<TransferRequest>()

    fun getContact(): LiveData<Resource<APIResponse<List<Contact>>?>> {
        return dataSource.getContact()
    }

    fun setSelectedContact(contact: Contact) {
        selectedContact.value = contact
    }

    fun getSelectedContact(): MutableLiveData<Contact> {
        return selectedContact
    }

    fun setTransferParam(data: TransferRequest) {
        transfer.value = data
    }

    fun getTransferParam(): MutableLiveData<TransferRequest> {
        return transfer
    }

    fun transfer(trf:TransferRequest,pin:String): LiveData<Resource<APIResponseTransfer<TransferResponseModel>?>> {
        return  dataSource.Transfer(trf,pin)
    }

    fun balance(): LiveData<Resource<APIResponse<List<UserDetail>>?>> {
        return dataSource.getBalance()
    }


}
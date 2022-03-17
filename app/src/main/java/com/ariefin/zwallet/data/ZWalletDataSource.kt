package com.ariefin.zwallet.data

import androidx.lifecycle.liveData
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.request.LoginRequest
import com.ariefin.zwallet.model.request.TransferRequest
import com.ariefin.zwallet.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ZWalletDataSource @Inject constructor(private val apiClient: ZWalletApi) {
    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val loginRequest = LoginRequest(email = email, password = password)
            val response = apiClient.login(loginRequest)
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getInvoice() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getInvoice()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getBalance() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getBalance()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getProfileInfo() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getProfile()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getContact() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = apiClient.getContact()
            emit(Resource.success(response))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun Transfer(transfer: TransferRequest, pin:String)= liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            val response =apiClient.transfer(transfer,pin)
            emit(Resource.success(response))
        }catch (e :Exception){
            emit(Resource.error(null,e.localizedMessage))
        }
    }
}
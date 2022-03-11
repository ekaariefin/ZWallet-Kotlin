package com.ariefin.zwallet.data

import androidx.lifecycle.liveData
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.Invoice
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.model.request.LoginRequest
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class ZWalletDataSource(private val apiClient: ZWalletApi) {
    fun login(email: String, password: String) = liveData<APIResponse<User>>(Dispatchers.IO) {
        try {
            val loginRequest = LoginRequest(email = email, password = password)
            val response = apiClient.login(loginRequest)
            emit(response)
        } catch (e: Exception) {
            emit(APIResponse(400,e.localizedMessage,null))
        }
    }

    fun getInvoice() = liveData<APIResponse<List<Invoice>>>(Dispatchers.IO) {
        try {
            val response = apiClient.getInvoice()
            emit(response)
        } catch (e: Exception) {
            emit(APIResponse(400,e.localizedMessage,null))
        }
    }

    fun getBalance() = liveData<APIResponse<List<UserDetail>>>(Dispatchers.IO) {
        try {
            val response = apiClient.getBalance()
            emit(response)
        } catch (e: Exception) {
            emit(APIResponse(400,e.localizedMessage,null))
        }
    }

}
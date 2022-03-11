package com.ariefin.zwallet.ui.auth.login

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.LoginRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.*
import javax.net.ssl.HttpsURLConnection

class LoginViewModel( app: Application): AndroidViewModel(app) {
    private var preferences: SharedPreferences = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
    private var apiClient: ZWalletApi = NetworkConfig(app).buildApi()

    fun login(email: String, password: String): APIResponse<User>? {
        val loginRequest = LoginRequest(email = email,  password = password)
        val response = apiClient.login(loginRequest).execute()
        if(response.isSuccessful) {
            if(response.body()?.status == HttpsURLConnection.HTTP_OK) {
                val res = response.body()!!.data

                with (preferences.edit()){
                    putBoolean(KEY_LOGGED_IN, true)
                    putString(KEY_USER_EMAIL, res?.email)
                    putString(KEY_USER_TOKEN, res?.token)
                    putString(KEY_USER_REFRESH_TOKEN, res?.refreshToken)
                    apply()
                }
            }
        }

        return response.body()
    }
}
package com.ariefin.zwallet.network

import android.content.Context
import com.ariefin.zwallet.data.api.ZWalletApi
import com.ariefin.zwallet.utils.BASE_URL
import com.ariefin.zwallet.utils.KEY_USER_TOKEN
import com.ariefin.zwallet.utils.PREFS_NAME
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig(val context: Context?) {
    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = prefs?.getString(KEY_USER_TOKEN, "")
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
        if(!token.isNullOrEmpty()){
            client.addInterceptor(TokenInterceptor(context))
        }

        return client.build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): ZWalletApi {
        return getRetrofit().create(ZWalletApi::class.java)
    }




}
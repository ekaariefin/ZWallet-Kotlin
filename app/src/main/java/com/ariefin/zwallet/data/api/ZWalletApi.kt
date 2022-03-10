package com.ariefin.zwallet.data.api

import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.Register
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.LoginRequest
import com.ariefin.zwallet.model.request.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ZWalletApi {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<APIResponse<User>>

    @POST("auth/signup")
    fun register(@Body request: RegisterRequest): Call<APIResponse<User>>

}
package com.ariefin.zwallet.data.api

import com.ariefin.zwallet.model.*
import com.ariefin.zwallet.model.request.HomeRequest
import com.ariefin.zwallet.model.request.LoginRequest
import com.ariefin.zwallet.model.request.RefreshTokenRequest
import com.ariefin.zwallet.model.request.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ZWalletApi {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<APIResponse<User>>

    @POST("auth/signup")
    fun register(@Body request: RegisterRequest): Call<APIResponse<User>>

    @GET("user/myProfile")
    fun getProfile():Call<APIResponse<UserDetail>>

    @GET("home/getBalance")
    fun getBalance():Call<APIResponse<ArrayList<Balance>>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<APIResponse<User>>

}
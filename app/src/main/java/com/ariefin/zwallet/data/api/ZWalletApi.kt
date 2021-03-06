package com.ariefin.zwallet.data.api

import com.ariefin.zwallet.model.*
import com.ariefin.zwallet.model.request.*
import retrofit2.Call
import retrofit2.http.*

interface ZWalletApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): APIResponse<User>

    @POST("auth/signup")
    fun register(@Body request: RegisterRequest): Call<APIResponse<User>>

    @GET("user/myProfile")
    suspend fun getProfile(): APIResponse<UserDetail>

    @GET("home/getBalance")
    suspend fun getBalance(): APIResponse<List<UserDetail>>

    @GET("home/getInvoice")
    suspend fun getInvoice(): APIResponse<List<Invoice>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<APIResponse<User>>

    @PATCH("user/changePassword")
    fun changePassword(@Body request: ChangePasswordRequest): Call<APIResponse<User>>

    @PATCH("auth/PIN")
    fun createPIN(@Body request: CreatePinRequest): Call<APIResponse<User>>

    @GET("tranfer/contactUser")
    suspend fun getContact(): APIResponse<List<Contact>>

    @POST("tranfer/newTranfer")
    suspend fun transfer(@Body Transfer:TransferRequest,@Header("x-access-PIN") pin:String):APIResponseTransfer<TransferResponseModel>





}
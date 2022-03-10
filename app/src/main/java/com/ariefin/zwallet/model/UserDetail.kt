package com.ariefin.zwallet.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

data class UserDetail(
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("firstname")
    @Expose
    val firstname: String?,
    @SerializedName("image")
    @Expose
    val image: String?,
    @SerializedName("lastname")
    @Expose
    val lastname: String?,
    @SerializedName("phone")
    @Expose
    val phone: String?
)
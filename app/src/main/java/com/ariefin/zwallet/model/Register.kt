package com.ariefin.zwallet.model


import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)
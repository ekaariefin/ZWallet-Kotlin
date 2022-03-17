package com.ariefin.zwallet.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class APIResponseTransfer<T>(
    @SerializedName("status")
    @Expose
    var status:Int,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("details")
    @Expose
    var details:T?)
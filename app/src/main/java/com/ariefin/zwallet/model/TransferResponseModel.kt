package com.ariefin.zwallet.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransferResponseModel(
    @SerializedName("amount")
    @Expose
    val amount: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("notes")
    @Expose
    val notes: String?,
    @SerializedName("receiver")
    @Expose
    val `receiver`: Int?,
    @SerializedName("sender")
    @Expose
    val sender: Int?,
    @SerializedName("type")
    @Expose
    val type: Int?
)
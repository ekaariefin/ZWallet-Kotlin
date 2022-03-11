package com.ariefin.zwallet.model


import com.google.gson.annotations.SerializedName

data class Invoice(
    @SerializedName("amount")
    val amount: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("receiver")
    val `receiver`: Int?,
    @SerializedName("sender")
    val sender: Int?,
    @SerializedName("type")
    val type: String?
)
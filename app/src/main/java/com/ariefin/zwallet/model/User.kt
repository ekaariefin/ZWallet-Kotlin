package com.ariefin.zwallet.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String?,
    @SerializedName("expired_at")
    val expiredAt: Long?,
    @SerializedName("expired_in")
    val expiredIn: Int?,
    @SerializedName("hasPin")
    val hasPin: Boolean?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("refreshToken_expired_at")
    val refreshTokenExpiredAt: Long?,
    @SerializedName("refreshToken_expired_in")
    val refreshTokenExpiredIn: Int?,
    @SerializedName("token")
    val token: String?
)
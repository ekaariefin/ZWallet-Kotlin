package com.ariefin.zwallet.model.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

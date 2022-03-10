package com.ariefin.zwallet.model

//status code:
// 200 -> Success
// 401 -> error/failed

data class APIResponse<T>(
    var status: Int,
    var message: String,
    var data: T
)

package com.ariefin.zwallet.network

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor(
    private val tokenProvider:() ->String)
    : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenProvider.invoke()
        if(!token.isNullOrEmpty()) {
            request.header("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}
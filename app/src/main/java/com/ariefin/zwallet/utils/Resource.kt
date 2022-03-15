package com.ariefin.zwallet.utils

class Resource<out T>(val state:State, val resource: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(State.SUCCESS, data, null)
        fun <T> error(data: T, message: String?): Resource<T> = Resource(State.ERROR, data, null)
        fun <T> loading(data: T): Resource<T> = Resource(State.LOADING, data, null)
    }
}
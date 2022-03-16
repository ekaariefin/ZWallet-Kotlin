package com.ariefin.zwallet.data.api
import com.ariefin.zwallet.model.Contact

interface PostClickHandler {
    fun clickedPostItem(post: Contact)
}
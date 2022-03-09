package com.ariefin.zwallet.data
import android.graphics.drawable.Drawable

//data class digunakan untuk menyimpan suatu data dan tidak memiliki function
data class Transaction(
    val transactionImage: Drawable,
    val transactionName: String,
    val transactionType: String,
    val transactionNominal: Double,
)


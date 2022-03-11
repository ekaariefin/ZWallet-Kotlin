package com.ariefin.zwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariefin.zwallet.R
import com.ariefin.zwallet.data.Transaction
import com.ariefin.zwallet.model.Invoice
import com.ariefin.zwallet.utils.BASE_URL
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class TransactionAdapter(private var data: List<Invoice>): RecyclerView.Adapter<TransactionAdapter.TransactionAdapterHolder>() {
    lateinit var contextAdapter: Context

    class TransactionAdapterHolder(view: View): RecyclerView.ViewHolder(view) {
        private val image: ShapeableImageView = view.findViewById(R.id.imageTransaction)
        private val name: TextView = view.findViewById(R.id.textTransactionName)
        private val type: TextView = view.findViewById(R.id.textTransactionType)
        private val amount: TextView = view.findViewById(R.id.textTransactionAmount)

        fun bindData(data: Invoice, context: Context, position: Int){
            name.text = data.name
            type.text = data.type?.uppercase()
            amount.formatPrice(data.amount.toString())
            Glide.with(image)
                .load(BASE_URL + data.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_remove_red_eye_24)
                )
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context

        val inflatedView: View = inflater.inflate(R.layout.item_transaction_home, parent, false)
        return TransactionAdapterHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TransactionAdapterHolder, x: Int) {
        holder.bindData(this.data[x], contextAdapter, x)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun addData(data: List<Invoice>) {
        this.data = data
    }
}
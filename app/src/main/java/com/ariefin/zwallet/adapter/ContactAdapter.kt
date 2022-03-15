package com.ariefin.zwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariefin.zwallet.R
import com.ariefin.zwallet.model.Contact
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.utils.BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter(private var data: List<Contact>): RecyclerView.Adapter<ContactAdapter.ContactAdapterHolder>() {
    lateinit var contextAdapter: Context

    class ContactAdapterHolder(view: View): RecyclerView.ViewHolder(view) {
        private val image: ShapeableImageView = view.findViewById(R.id.contactImage)
        private val name: TextView = view.findViewById(R.id.contactName)
        private val phone: TextView = view.findViewById(R.id.contactPhoneNum)

        fun bindData(data: Contact, context: Context, position: Int){
            name.text = data.name
            phone.text = data.phone
            Glide.with(image)
                .load(BASE_URL + data.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_remove_red_eye_24)
                )
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context

        val inflatedView: View = inflater.inflate(R.layout.contact_transaction, parent, false)
        return ContactAdapterHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ContactAdapterHolder, x: Int) {
        holder.bindData(this.data[x], contextAdapter, x)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun addData(data: List<Contact>) {
        this.data = data
    }


}
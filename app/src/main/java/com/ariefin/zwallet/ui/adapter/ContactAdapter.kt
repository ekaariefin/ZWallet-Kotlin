package com.ariefin.zwallet.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariefin.zwallet.R
import com.ariefin.zwallet.data.api.PostClickHandler
import com.ariefin.zwallet.databinding.ItemContactTransferBinding
import com.ariefin.zwallet.model.Contact
import com.ariefin.zwallet.utils.BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter(
    private var data: List<Contact>,
    private val clickListener: (Contact, View) -> Unit,
): RecyclerView.Adapter<ContactAdapter.ContactAdapterHolder>() {
    private lateinit var contextAdapter: Context

    class ContactAdapterHolder(private val binding: ItemContactTransferBinding): RecyclerView
    .ViewHolder(binding.root) {
        fun bindData(data: Contact, onClick: (Contact, View) -> Unit){
            binding.contactName.text = data.name
            binding.contactPhoneNum.text = data.phone
            Glide.with(binding.contactImage)
                .load(BASE_URL + data.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_remove_red_eye_24)
                )
                .into(binding.contactImage)

            binding.root.setOnClickListener { onClick(data, binding.root) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context
        val binding = ItemContactTransferBinding.inflate(inflater, parent, false)
        return ContactAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactAdapterHolder, x: Int) {
        holder.bindData(this.data[x], clickListener)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun addData(data: List<Contact>) {
        this.data = data
    }
}
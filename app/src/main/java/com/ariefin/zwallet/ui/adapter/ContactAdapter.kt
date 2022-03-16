package com.ariefin.zwallet.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ariefin.zwallet.R
import com.ariefin.zwallet.data.api.PostClickHandler
import com.ariefin.zwallet.model.Contact
import com.ariefin.zwallet.utils.BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter(
    private var values: List<Contact>,
    private val clickHandler: PostClickHandler
) : RecyclerView.Adapter<ContactAdapter.ContactAdapterHolder>() {
    lateinit var contextAdapter: Context

    inner class ContactAdapterHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val image: ShapeableImageView = view.findViewById(R.id.contactImage)
        val name: TextView = view.findViewById(R.id.contactName)
        val phone: TextView = view.findViewById(R.id.contactPhoneNum)

        init {
            view.setOnClickListener(this)
        }

        fun bindvalues(values: Contact, context: Context, position: Int){
            name.text = values.name
            phone.text = values.phone
            Glide.with(image)
                .load(BASE_URL + values.image)
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_remove_red_eye_24)
                )
                .into(image)
        }

        override fun onClick(p0: View?) {
            val currentPost = values[bindingAdapterPosition]
            clickHandler.clickedPostItem(currentPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        this.contextAdapter = parent.context

        val inflatedView: View = inflater.inflate(R.layout.contact_transaction, parent, false)
        return ContactAdapterHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ContactAdapterHolder, x: Int) {
        holder.bindvalues(this.values[x], contextAdapter, x)

    }

    override fun getItemCount(): Int {
        return this.values.size
    }

    fun addData(values: List<Contact>) {
        this.values = values
    }


}
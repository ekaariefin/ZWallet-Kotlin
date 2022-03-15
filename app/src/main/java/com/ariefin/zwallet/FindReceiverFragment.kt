package com.ariefin.zwallet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariefin.zwallet.adapter.ContactAdapter
import com.ariefin.zwallet.adapter.TransactionAdapter
import com.ariefin.zwallet.databinding.FragmentCreatePinBinding
import com.ariefin.zwallet.databinding.FragmentFindReceiverBinding
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import com.ariefin.zwallet.widget.LoadingDialog
import javax.net.ssl.HttpsURLConnection

class FindReceiverFragment : Fragment() {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentFindReceiverBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: FindReceiverViewModel by viewModelsFactory { FindReceiverViewModel(requireActivity().application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindReceiverBinding.inflate(layoutInflater)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData()

    }

    private fun prepareData() {
        this.contactAdapter = ContactAdapter(listOf())
        binding.recyclerTransactionContact.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
        }
            viewModel.getContact().observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        binding.apply {
                            loadingIndicator.visibility = View.VISIBLE
                            recyclerTransactionContact.visibility = View.GONE
                        }
                    }
                    State.SUCCESS -> {
                        binding.apply {
                            loadingIndicator.visibility = View.GONE
                            recyclerTransactionContact.visibility = View.VISIBLE
                        }
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            this.contactAdapter.apply {
                                addData(it.resource?.data!!)
                                notifyDataSetChanged()
                            }
                        } else {
                            Toast.makeText(context, it.resource?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    State.ERROR -> {
                        binding.apply {
                            loadingIndicator.visibility = View.GONE
                            recyclerTransactionContact.visibility = View.VISIBLE
                        }
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }


        }
    }
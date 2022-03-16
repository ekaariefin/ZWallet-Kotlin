package com.ariefin.zwallet.ui.layout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariefin.zwallet.R
import com.ariefin.zwallet.data.api.PostClickHandler
import com.ariefin.zwallet.databinding.FragmentFindReceiverBinding
import com.ariefin.zwallet.model.Contact
import com.ariefin.zwallet.ui.adapter.ContactAdapter
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

private const val TAG = "PostFragment"

@AndroidEntryPoint
class FindReceiverFragment : Fragment(), PostClickHandler {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentFindReceiverBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: TransferViewModel by activityViewModels()

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
        this.contactAdapter = ContactAdapter(listOf(),this)
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

    override fun clickedPostItem(post: Contact) {
        Log.d(TAG, "clickedPostItem: ${post.name}")
        val bundle = bundleOf(
            Pair("name", post.name),
            Pair("phone", post.phone)
        )
        findNavController().navigate(R.id.action_findReceiverFragment_to_transferFragment, bundle)


    }

}
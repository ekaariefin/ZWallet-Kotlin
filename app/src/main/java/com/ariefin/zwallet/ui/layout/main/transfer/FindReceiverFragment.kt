package com.ariefin.zwallet.ui.layout.main.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentFindReceiverBinding
import com.ariefin.zwallet.ui.adapter.ContactAdapter
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection


@AndroidEntryPoint
class FindReceiverFragment : Fragment() {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentFindReceiverBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindReceiverBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.contactAdapter = ContactAdapter(listOf())
        { contact, _ ->
            viewModel.setSelectedContact(contact)
            Navigation.findNavController(view)
                .navigate(R.id.action_findReceiverFragment_to_transferFragment)
        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view)
                .popBackStack()
        }

        binding.recyclerTransactionContact.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactAdapter
        }
        viewModel.getContact().observe(viewLifecycleOwner) {
            when (it.state){
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
                            addData(it.resource.data!!)
                            notifyDataSetChanged()
                        }
                        binding.contactTotal.text = contactAdapter.itemCount.toString()
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
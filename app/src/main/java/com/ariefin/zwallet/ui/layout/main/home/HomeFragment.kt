package com.ariefin.zwallet.ui.layout.main.home

import android.content.Context
import android.content.SharedPreferences
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
import com.ariefin.zwallet.databinding.FragmentHomeBinding
import com.ariefin.zwallet.ui.adapter.TransactionAdapter
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.profileImage.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment2_to_userFragment)
        }

        binding.buttonTopUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment2_to_topUpFragment3)
        }

        binding.buttonTransfer.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment2_to_findReceiverFragment)
        }

    }

    private fun prepareData() {
        this.transactionAdapter = TransactionAdapter(listOf())
        binding.recyclerTransaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            when (it.state) {
                State.LOADING -> {
                    loadingDialog.start("Processing your request")
                    binding.apply {
                        loadingIndicator.visibility = View.VISIBLE
                        recyclerTransaction.visibility = View.GONE
                    }
                }

                State.SUCCESS -> {

                    if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                        binding.apply {
                            currentBalance.formatPrice(it.resource.data?.get(0)?.balance.toString())
                            userPhoneNum.text = it.resource.data?.get(0)?.phone
                            userNameInfo.text = it.resource.data?.get(0)?.name
                        }
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    loadingDialog.dismiss()
                }

                State.ERROR -> {
                    binding.apply {
                        loadingIndicator.visibility = View.GONE
                        recyclerTransaction.visibility = View.VISIBLE
                    }
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }


            }

            viewModel.getInvoice().observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        binding.apply {
                            loadingIndicator.visibility = View.VISIBLE
                            recyclerTransaction.visibility = View.GONE
                        }
                    }
                    State.SUCCESS -> {
                        binding.apply {
                            loadingIndicator.visibility = View.GONE
                            recyclerTransaction.visibility = View.VISIBLE
                        }
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            this.transactionAdapter.apply {
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
                            recyclerTransaction.visibility = View.VISIBLE
                        }
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }


        }
    }
}
package com.ariefin.zwallet.ui.main.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariefin.zwallet.R
import com.ariefin.zwallet.SplashScreenActivity
import com.ariefin.zwallet.adapter.TransactionAdapter
import com.ariefin.zwallet.data.Transaction
import com.ariefin.zwallet.databinding.FragmentHomeBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.Balance
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.KEY_LOGGED_IN
import com.ariefin.zwallet.utils.PREFS_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.Helper.formatPrice
import javax.net.ssl.HttpsURLConnection

class HomeFragment : Fragment() {
    private val transactionData = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()
        getProfile()


        binding.profileImage.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment2_to_userFragment)
        }

        binding.notificationIcon.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    with(prefs.edit()) {
                        putBoolean(KEY_LOGGED_IN, false)
                        apply()
                    }
                    val intent = Intent(activity, SplashScreenActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
                .show()
        }
    }

    private fun getProfile() {
        NetworkConfig(requireContext()).buildApi()
            .getProfile()
            .enqueue(object : Callback<APIResponse<UserDetail>> {
                override fun onResponse(
                    call: Call<APIResponse<UserDetail>>,
                    response: Response<APIResponse<UserDetail>>
                ) {
                    binding.userNameInfo.text = response.body()?.data?.firstname.toString()
                }

                override fun onFailure(call: Call<APIResponse<UserDetail>>, t: Throwable) {
                    Toast.makeText(requireContext(), "gagal", Toast.LENGTH_LONG).show()
                }
            })
    }


    private fun prepareData() {
       this.transactionAdapter = TransactionAdapter(listOf())
        binding.recyclerTransaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if(it.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    currentBalance.formatPrice(it.data?.get(0)?.balance.toString())
                    userPhoneNum.text = it.data?.get(0)?.phone
                }
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.getInvoice().observe(viewLifecycleOwner) {
            if(it.status == HttpsURLConnection.HTTP_OK) {
                this.transactionAdapter.apply {
                    addData(it.data!!)
                    notifyDataSetChanged()
                }
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
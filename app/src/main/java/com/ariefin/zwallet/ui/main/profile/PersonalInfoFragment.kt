package com.ariefin.zwallet.ui.main.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariefin.zwallet.R
import com.ariefin.zwallet.adapter.TransactionAdapter
import com.ariefin.zwallet.data.Transaction
import com.ariefin.zwallet.databinding.FragmentHomeBinding
import com.ariefin.zwallet.databinding.FragmentPersonalInfoBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.UserDetail
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.ariefin.zwallet.utils.PREFS_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class PersonalInfoFragment : Fragment() {
    private lateinit var binding: FragmentPersonalInfoBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnManagePhoneNum.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_personalInfoFragment_to_managePhoneNumFragment)
        }

        prepareData()

    }

    private fun prepareData() {

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if(it.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    firstNameValue.text = it.data?.get(0)?.firstname
                    lastNameValue.text = it.data?.get(0)?.lastname
                    verifiedEmailValue.text = it.data?.get(0)?.email
                    phoneNumberValue.text = it.data?.get(0)?.phone
                }
            } else {
                Toast.makeText(context, "Terjadi Kesalahan Saat Memproses Data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
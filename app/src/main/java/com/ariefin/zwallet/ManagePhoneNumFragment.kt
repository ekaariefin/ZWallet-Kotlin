package com.ariefin.zwallet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ariefin.zwallet.databinding.FragmentManagePhoneNumBinding
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.PREFS_NAME
import javax.net.ssl.HttpsURLConnection


class ManagePhoneNumFragment : Fragment() {
    private lateinit var binding: FragmentManagePhoneNumBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagePhoneNumBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()
    }

    private fun prepareData() {

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if(it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    phoneNumberValue.text = it.resource?.data?.get(0)?.phone
                }
            } else {
                Toast.makeText(context, "Terjadi Kesalahan Saat Memproses Data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
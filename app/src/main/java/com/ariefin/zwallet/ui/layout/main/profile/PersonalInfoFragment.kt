package com.ariefin.zwallet.ui.layout.main.profile

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
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentPersonalInfoBinding
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {
    private lateinit var binding: FragmentPersonalInfoBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: PersonalInfoViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalInfoBinding.inflate(layoutInflater)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        prepareData()

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view)
                .popBackStack()
        }

        binding.btnManagePhoneNum.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_personalInfoFragment_to_managePhoneNumFragment)
        }

        binding.btnAddPhoneNum.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_personalInfoFragment_to_addPhoneNumberFragment)
        }

    }

    private fun prepareData() {
        viewModel.getProfileInfo().observe(viewLifecycleOwner) {
            when (it.state) {
                State.LOADING -> {
                    loadingDialog.start("Processing data")
                }

                State.SUCCESS -> {
                    if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                        binding.apply {
                            firstNameValue.text = it.resource.data?.firstname
                            lastNameValue.text = it.resource.data?.lastname
                            verifiedEmailValue.text = it.resource.data?.email
                            phoneNumberValue.text = it.resource.data?.phone

                            if(binding.phoneNumberValue.text.isNullOrEmpty()) {
                                binding.layoutPhoneNumWithData.visibility = View.GONE
                                binding.layoutPhoneNumNoData.visibility = View.VISIBLE
                            }
                            else {
                                binding.layoutPhoneNumWithData.visibility = View.VISIBLE
                                binding.layoutPhoneNumNoData.visibility = View.GONE
                            }

                        }
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    loadingDialog.dismiss()
                }

                State.ERROR -> {
                    loadingDialog.dismiss()
                    Toast.makeText(context, "Terjadi Kesalahan Saat Mengambil Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
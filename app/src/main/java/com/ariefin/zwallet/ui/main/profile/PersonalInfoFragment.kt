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
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentPersonalInfoBinding
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import com.ariefin.zwallet.widget.LoadingDialog
import javax.net.ssl.HttpsURLConnection

class PersonalInfoFragment : Fragment() {
    private lateinit var binding: FragmentPersonalInfoBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: PersonalInfoViewModel by viewModelsFactory { PersonalInfoViewModel(requireActivity().application) }
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

        binding.btnManagePhoneNum.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_personalInfoFragment_to_managePhoneNumFragment)
        }
        prepareData()
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
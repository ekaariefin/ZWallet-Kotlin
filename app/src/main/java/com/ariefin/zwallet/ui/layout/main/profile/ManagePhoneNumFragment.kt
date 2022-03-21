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
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentManagePhoneNumBinding
import com.ariefin.zwallet.model.request.ChangeInfoRequest
import com.ariefin.zwallet.ui.layout.main.home.HomeViewModel
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ManagePhoneNumFragment : Fragment() {
    private lateinit var binding: FragmentManagePhoneNumBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: PersonalInfoViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagePhoneNumBinding.inflate(layoutInflater)
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

        binding.btnDeletePhoneNum.setOnClickListener {
            viewModel.changeInfo(ChangeInfoRequest("")).observe(viewLifecycleOwner, Observer {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Memproses Data...")
                    }
                    State.SUCCESS -> {
                        loadingDialog.dismiss()
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            Toast.makeText(context, "Hapus Ponsel Berhasil", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(view)
                                .popBackStack()
                        }
                    }
                    State.ERROR -> {
                        loadingDialog.dismiss()
                        Toast.makeText(context, "Terjadi Kesalahan!", Toast.LENGTH_SHORT)
                            .show()
                        Navigation.findNavController(view)
                            .popBackStack()
                    }
                }
            })
        }
    }

    private fun prepareData() {
        viewModel.getProfileInfo().observe(viewLifecycleOwner) {
            if(it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    phoneNumberValue.text = it.resource.data?.phone.toString()
                }
            }
        }
    }



}
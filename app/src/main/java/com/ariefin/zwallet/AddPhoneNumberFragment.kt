package com.ariefin.zwallet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.ariefin.zwallet.databinding.FragmentAddPhoneNumberBinding
import com.ariefin.zwallet.databinding.FragmentPersonalInfoBinding
import com.ariefin.zwallet.model.request.ChangeInfoRequest
import com.ariefin.zwallet.ui.layout.main.profile.PersonalInfoViewModel
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import javax.net.ssl.HttpsURLConnection

class AddPhoneNumberFragment : Fragment() {
    private lateinit var binding: FragmentAddPhoneNumberBinding
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
        binding = FragmentAddPhoneNumberBinding.inflate(layoutInflater)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view)
                .popBackStack()
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.changeInfo(ChangeInfoRequest(binding.inputNewPhoneNum.text.toString())).observe(viewLifecycleOwner, Observer {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Memproses Data...")
                    }
                    State.SUCCESS -> {
                        loadingDialog.dismiss()
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            Toast.makeText(context, "Proses Tambah Ponsel Berhasil", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(view)
                                .navigate(R.id.action_addPhoneNumberFragment_to_personalInfoFragment)
                        }

                    }
                    State.ERROR -> {
                        loadingDialog.dismiss()
                        Toast.makeText(context, "Terjadi kesalahan!", Toast.LENGTH_SHORT)
                            .show()
                        Navigation.findNavController(view)
                            .navigate(R.id.action_addPhoneNumberFragment_to_personalInfoFragment)

                    }
                }
            })
        }
    }
}
package com.ariefin.zwallet.ui.layout.main.transfer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentTransferBinding
import com.ariefin.zwallet.model.request.TransferRequest
import com.ariefin.zwallet.utils.BASE_URL
import com.ariefin.zwallet.utils.PREFS_NAME
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding
    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var id: String?=null

        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            id = it?.id.toString()
            binding.apply {
                transferUserName.text = "${it?.name}"
                transferUserPhone.text = "${it?.phone}"
                Glide.with(transferUserImage)
                    .load(BASE_URL + it?.image)
                    .apply(
                        RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_baseline_remove_red_eye_24)
                    )
                    .into(transferUserImage)
            }
        }

        binding.btnContinueToPin.setOnClickListener {
            viewModel.setTransferParam(
                TransferRequest(id!!,
                    binding.inputTransferAmount.text.toString().toInt(),
                    binding.inputTransferDesc.text.toString()
                ))

            Navigation.findNavController(view)
                .navigate(R.id.action_transferFragment_to_confirmationFragment)
        }

    }

}

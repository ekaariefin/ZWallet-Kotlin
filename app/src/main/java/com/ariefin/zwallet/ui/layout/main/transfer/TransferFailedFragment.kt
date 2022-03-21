package com.ariefin.zwallet.ui.layout.main.transfer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentTransferFailedBinding
import com.ariefin.zwallet.ui.layout.main.MainActivity
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.BASE_URL
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.ariefin.zwallet.utils.PREFS_NAME
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.net.ssl.HttpsURLConnection

class TransferFailedFragment : Fragment() {
    private lateinit var binding: FragmentTransferFailedBinding
    private lateinit var preferences: SharedPreferences
    private val viewModel: TransferViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferFailedBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            binding.apply {
                selectedContactName.text = "${it?.name}"
                selectedContactPhoneNum.text = "${it?.phone}"
                Glide.with(selectedContactImage)
                    .load(BASE_URL + it?.image)
                    .apply(
                        RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_baseline_remove_red_eye_24)
                    )
                    .into(selectedContactImage)
            }
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    confirmationBalanceLeftValue.formatPrice(it.resource.data?.get(0)?.balance.toString())
                }
            }
        }

        viewModel.getTransferParam().observe(viewLifecycleOwner) {
            binding.confirmationAmountValue.formatPrice(it.amount.toString())
            binding.confirmationBalanceLeftValue.formatPrice(it.amount.toString())
            if (it.notes.isNullOrEmpty()) {
                binding.notesValue.text = "-"
            } else {
                binding.notesValue.text = it.notes
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mma")
                val answer =  current.format(formatter)
                binding.dateAndTimeValue.text = answer
            } else {
                val date = Date()
                val formatter = SimpleDateFormat("MMM dd, yyyy - HH:mma")
                val answer = formatter.format(date)
                binding.dateAndTimeValue.text = answer
            }
        }

        binding.btnTryAgain.setOnClickListener {
            Handler().postDelayed({
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }, 500)
        }
    }

}
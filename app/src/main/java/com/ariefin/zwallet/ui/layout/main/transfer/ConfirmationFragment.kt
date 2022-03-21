package com.ariefin.zwallet.ui.layout.main.transfer

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentConfirmationBinding
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.BASE_URL
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class ConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentConfirmationBinding
    private val viewModel: TransferViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(layoutInflater)
        loadingDialog= LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view)
                .popBackStack()
        }

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    confirmationBalanceLeftValue.formatPrice(it.resource.data?.get(0)?.balance.toString())
                }
            }
        }

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

        binding.btnContinueToPin.setOnClickListener {
            loadingDialog.start("Memproses Transaksi Anda, Harap Menunggu...")
            AlertDialog.Builder(context)
                .setTitle("Transfer Confirmation")
                .setMessage("Are you sure you want to make the transfer process?")
                .setPositiveButton("Yes") { _, _ ->
                    Navigation.findNavController(it).navigate(R.id.action_confirmationFragment_to_pinConfirmationFragment)
                    loadingDialog.stop()

                }.setNegativeButton("No") { _, _ ->
                    return@setNegativeButton
                }.show()
            loadingDialog.dismiss()
        }
    }

}
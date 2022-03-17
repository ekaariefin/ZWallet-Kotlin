package com.ariefin.zwallet.ui.layout.main.transfer

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentConfirmationBinding
import com.ariefin.zwallet.databinding.FragmentPinConfirmationBinding
import com.ariefin.zwallet.model.request.TransferRequest
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.BASE_URL
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class PinConfirmationFragment : Fragment() {
    private  lateinit var preferences: SharedPreferences
    private lateinit  var loadingDialog: LoadingDialog
    var pin  = mutableListOf<EditText>()
    private lateinit var binding: FragmentPinConfirmationBinding
    private  val viewModel: TransferViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinConfirmationBinding.inflate(layoutInflater)
        loadingDialog= LoadingDialog(requireActivity())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditText()
        preferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnContinueTransferProcess.setOnClickListener {

            transfer(it)

        }
    }

    fun initEditText(){
        pin.add(0,binding.inputPin1)
        pin.add(1,binding.inputPin2)
        pin.add(2,binding.inputPin3)
        pin.add(3,binding.inputPin4)
        pin.add(4,binding.inputPin5)
        pin.add(5,binding.inputPin6)
        pinHandler()
    }

    private fun pinHandler(){
        for (i in 0..5) {
            pin[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }
                override fun afterTextChanged(s: Editable) {
                    if (i == 5 && pin[i].text.toString().isNotEmpty()) {
                        pin[i].clearFocus()
                    } else if (pin[i].text.toString().isNotEmpty()) {
                        pin[i + 1].requestFocus()
                    }
                }

            })

            pin[i].setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action !== KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener false //Dont get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                }
                if (keyCode == KeyEvent.KEYCODE_DEL && pin[i].text.toString().isEmpty() && i != 0) {
                    //this condition is to handel the delete input by users.
                    pin[i - 1].setText("") //Deletes the digit of pin
                    pin[i - 1].requestFocus()
                    //and sets the focus on previous digit
                }
                false
            })

        }

    }

    fun getpin():String{
        return pin[0].text.toString()+
                pin[1].text.toString()+
                pin[2].text.toString()+
                pin[3].text.toString()+
                pin[4].text.toString()+
                pin[5].text.toString()
    }

    fun transfer(view: View) {
        var amount: String ?= null
        var notes: String ?= null
        var receiver: String ?= null
        var request: TransferRequest ?= null

        viewModel.getSelectedContact().observe(viewLifecycleOwner) {
            binding.apply {
                var transferUserName = "${it?.name}"
                var transferUserPhone = "${it?.phone}"
            }
        }

        viewModel.getTransferParam().observe(viewLifecycleOwner) {
            request = it
            binding.apply {
                receiver = "${it.receiver}"
                amount = "${it.amount}"
                notes = "${it.notes}"
            }
        }

        viewModel.transfer(request!!,getpin()).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.state){
                State.LOADING -> {
                    loadingDialog.start("Processing your request")
                }
                State.SUCCESS -> {
                    Toast.makeText(context, "Memuat Data...", Toast.LENGTH_SHORT)
                        .show()
                }
                State.ERROR -> {
                    Toast.makeText(context, "Gagal Memuat Data...", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })


    }


}
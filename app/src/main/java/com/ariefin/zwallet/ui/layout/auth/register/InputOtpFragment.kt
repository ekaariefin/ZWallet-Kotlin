package com.ariefin.zwallet.ui.layout.auth.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentInputOtpBinding
import com.ariefin.zwallet.ui.layout.auth.AuthActivity
import com.ariefin.zwallet.ui.layout.auth.AuthViewModel
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.KEY_USER_EMAIL
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputOtpFragment : Fragment() {
    private  lateinit var preferences: SharedPreferences
    private lateinit  var loadingDialog: LoadingDialog
    var otp  = mutableListOf<EditText>()
    private lateinit var binding: FragmentInputOtpBinding
    private  val viewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputOtpBinding.inflate(layoutInflater)
        loadingDialog= LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditText()
        preferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnConfirm.setOnClickListener {
            activation(it)
        }
    }

    fun initEditText(){
        otp.add(0,binding.inputOtp1)
        otp.add(1,binding.inputOtp2)
        otp.add(2,binding.inputOtp3)
        otp.add(3,binding.inputOtp4)
        otp.add(4,binding.inputOtp5)
        otp.add(5,binding.inputOtp6)
        otpHandler()
    }

    fun otpHandler(){
        for (i in 0..5) {
            otp.get(i).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    otp.get(i).setBackgroundResource(R.drawable.pin_input_filled_background)

                }
                override fun afterTextChanged(s: Editable) {
                    if (i == 5 && !otp.get(i).getText().toString().isEmpty()) {
                        otp.get(i).clearFocus()

                    } else if (!otp.get(i).getText().toString().isEmpty()) {
                        otp.get(i + 1).requestFocus()
                    }
                }
            })
            otp.get(i).setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action !== KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener false
                }
                if (keyCode == KeyEvent.KEYCODE_DEL && otp.get(i).getText().toString().isEmpty() && i != 0) {
                    otp.get(i - 1).setText("")
                    otp.get(i - 1).requestFocus()
                    otp.get(i).setBackgroundResource(R.drawable.pin_input_background)
                }
                false
            })
        }
    }

    fun getotp():String{
        return otp[0].text.toString()+
                otp[1].text.toString()+
                otp[2].text.toString()+
                otp[3].text.toString()+
                otp[4].text.toString()+
                otp[5].text.toString()
    }

    fun activation(view: View) {
        val email = preferences.getString(KEY_USER_EMAIL,"null")
        viewModel.activation(email!!,getotp()).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.state){
                State.LOADING -> {
                    loadingDialog.start("Memvalidasi Permintaan Anda...")
                }
                State.SUCCESS -> {
                    Toast.makeText(context, "Memproses Permintaan Anda, Harap Menunggu...", Toast.LENGTH_SHORT)
                        .show()
                    Toast.makeText(context, "Verifikasi OTP Berhasil! Silahkan Login", Toast.LENGTH_SHORT)
                        .show()
                    Handler().postDelayed({
                        val intent = Intent(activity, AuthActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }, 1000)
                    loadingDialog.stop()
                }
                State.ERROR -> {
                    loadingDialog.stop()
                    Toast.makeText(context, "Verifikasi Gagal, Harap Coba Lagi", Toast.LENGTH_SHORT)
                        .show()
                    Navigation.findNavController(view).navigate(R.id.action_inputOtpFragment_self)
                }
            }
        })
    }

}


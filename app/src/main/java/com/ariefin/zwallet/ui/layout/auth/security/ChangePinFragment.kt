package com.ariefin.zwallet.ui.layout.auth.security

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentChangePinBinding
import com.ariefin.zwallet.ui.layout.auth.AuthViewModel
import com.ariefin.zwallet.ui.layout.main.MainActivity
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection
import android.view.KeyEvent as KeyEvent1

@AndroidEntryPoint
class ChangePinFragment : Fragment() {
    private lateinit var binding: FragmentChangePinBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog
    var pin  = mutableListOf<EditText>()
    private val viewModel: AuthViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePinBinding.inflate(layoutInflater)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditText()

        binding.buttonContinueToNewPin.setOnClickListener {
            val response = viewModel.checkPIN(
                getpin()
            )
            response.observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Memvalidasi data...")
                    }
                    State.SUCCESS -> {
                        loadingDialog.dismiss()
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            Toast.makeText(context, "Validasi Berhasil", Toast.LENGTH_SHORT)
                                .show()
                            Navigation.findNavController(view)
                                .navigate(R.id.action_changePinFragment_to_fragmentInputNewPin)
                        }
                    }
                    State.ERROR -> {
                        Toast.makeText(context, "PIN yang dimasukkan salah!", Toast.LENGTH_SHORT)
                            .show()
                        loadingDialog.dismiss()
                    }
                }

            }
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

    fun pinHandler(){
        for (i in 0..5) { //Its designed for 6 digit pin
            pin.get(i).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    pin.get(i).setBackgroundResource(R.drawable.pin_input_filled_background)

                }
                override fun afterTextChanged(s: Editable) {
                    if (i == 5 && !pin.get(i).getText().toString().isEmpty()) {
                        pin.get(i).clearFocus()

                        //Clears focus when you have entered the last digit of the pin.
                    } else if (!pin.get(i).getText().toString().isEmpty()) {
                        pin.get(i + 1).requestFocus() //focuses on the next edittext after a digit is entered.
                    }
                }
            })
            pin.get(i).setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action !== android.view.KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener false //Dont get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                }
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && pin.get(i).getText().toString().isEmpty() && i != 0) {
                    //this condition is to handel the delete input by users.
                    pin.get(i - 1).setText("") //Deletes the digit of pin
                    pin.get(i - 1).requestFocus()
                    pin.get(i - 1).setBackgroundResource(R.drawable.pin_input_background)

                    //and sets the focus on previous digit
                }
                false
            })

        }


    }

    fun getpin():String{
        return pin[0].text.toString()+pin[1].text.toString()+pin[2].text.toString()+pin[3].text.toString()+pin[4].text.toString()+pin[5].text.toString()
    }




}
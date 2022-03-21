package com.ariefin.zwallet.ui.layout.auth.security

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.ariefin.zwallet.databinding.FragmentInputNewPinBinding
import com.ariefin.zwallet.model.request.CreatePinRequest
import com.ariefin.zwallet.ui.layout.auth.AuthViewModel
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentInputNewPin : Fragment() {
    private lateinit var binding: FragmentInputNewPinBinding
    private lateinit var preferences:  SharedPreferences
    var pin  = mutableListOf<EditText>()
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputNewPinBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        initEditText()

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view)
                .popBackStack()
        }

        binding.btnConfirmChangePIN.setOnClickListener() {
            val createpin = CreatePinRequest(
                getpin().toString()
            )
            viewModel.createPIN(createpin).observe(viewLifecycleOwner) {
                when (it.state){
                    State.LOADING -> {
                        loadingDialog.start("Proses Mengganti PIN Anda...")
                    }
                    State.SUCCESS -> {
                        Toast.makeText(context, "Ganti PIN Berhasil", Toast.LENGTH_SHORT)
                            .show()
                        Navigation.findNavController(view).navigate(R.id.action_fragmentInputNewPin_to_homeFragment2)
                        loadingDialog.stop()
                    }
                    State.ERROR -> {
                        Toast.makeText(context, "Ganti PIN Gagal! Harap Coba Lagi", Toast.LENGTH_SHORT)
                            .show()
                        Navigation.findNavController(view).navigate(R.id.action_fragmentInputNewPin_to_userFragment)
                        loadingDialog.stop()
                    }
                }

            }
        }
    }

    fun initEditText(){
        pin.add(0,binding.inputNewPin1)
        pin.add(1,binding.inputNewPin2)
        pin.add(2,binding.inputNewPin3)
        pin.add(3,binding.inputNewPin4)
        pin.add(4,binding.inputNewPin5)
        pin.add(5,binding.inputNewPin6)
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
                if (event.action !== KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener false //Dont get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                }
                if (keyCode == KeyEvent.KEYCODE_DEL && pin.get(i).getText().toString().isEmpty() && i != 0) {
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

    private fun getpin():String{
        return pin[0].text.toString()+
                pin[1].text.toString()+
                pin[2].text.toString()+
                pin[3].text.toString()+
                pin[4].text.toString()+
                pin[5].text.toString()
    }

}
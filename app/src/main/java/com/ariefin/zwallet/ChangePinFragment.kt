package com.ariefin.zwallet

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ariefin.zwallet.databinding.FragmentChangePinBinding
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.KEY_USER_EMAIL
import android.view.KeyEvent as KeyEvent1


class ChangePinFragment : Fragment() {
    private lateinit var binding: FragmentChangePinBinding
    private lateinit var prefs: SharedPreferences
    var pin  = mutableListOf<EditText>()
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonContinueToNewPin.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_changePinFragment_to_fragmentInputNewPin)
        }

        initEditText()
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

                        //Clears focus when you have entered the last digit of the pin.
                    } else if (pin[i].text.toString().isNotEmpty()) {
                        pin[i + 1].requestFocus() //focuses on the next edittext after a digit is entered.
                    }
                }

            })
            
            pin[i].setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (event.action !== KeyEvent1.ACTION_DOWN) {
                    return@OnKeyListener false //Dont get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                }
                if (keyCode == KeyEvent1.KEYCODE_DEL && pin[i].text.toString().isEmpty() && i != 0) {
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
        return pin[0].text.toString()+pin[1].text.toString()+pin[2].text.toString()+pin[3].text.toString()+pin[4].text.toString()+pin[5].text.toString()
    }




}
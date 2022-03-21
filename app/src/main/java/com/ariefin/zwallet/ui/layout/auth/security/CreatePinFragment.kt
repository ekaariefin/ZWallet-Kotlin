package com.ariefin.zwallet.ui.layout.auth.security

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
import com.ariefin.zwallet.databinding.FragmentCreatePinBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.CreatePinRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.ui.layout.auth.AuthActivity
import com.ariefin.zwallet.ui.layout.auth.AuthViewModel
import com.ariefin.zwallet.ui.layout.main.MainActivity
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class CreatePinFragment : Fragment() {
    private lateinit var binding: FragmentCreatePinBinding
    private lateinit var prefs: SharedPreferences
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
        binding = FragmentCreatePinBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditText()

        binding.btnConfirmCreatePin.setOnClickListener() {
            val createpin = CreatePinRequest(
                getpin().toString()
            )
            viewModel.createPIN(createpin).observe(viewLifecycleOwner) {
                when (it.state){
                    State.LOADING -> {
                        loadingDialog.start("Memproses Permintaan Anda...")
                    }
                    State.SUCCESS -> {
                        Toast.makeText(context, "Proses Aktivasi Berhasil, Silahkan Login", Toast.LENGTH_SHORT)
                            .show()
                        Handler().postDelayed({
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }, 1000)
                        loadingDialog.dismiss()
                    }
                    State.ERROR -> {
                        Toast.makeText(context, "Terjadi Kesalahan saat memproses data, Harap Ulangi Proses", Toast.LENGTH_SHORT)
                            .show()
                        Handler().postDelayed({
                            val intent = Intent(activity, AuthActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }, 1000)
                        loadingDialog.dismiss()
                    }
                }

            }
        }
    }

    fun initEditText(){
        pin.add(0,binding.inputRegisterPin1)
        pin.add(1,binding.inputRegisterPin2)
        pin.add(2,binding.inputRegisterPin3)
        pin.add(3,binding.inputRegisterPin4)
        pin.add(4,binding.inputRegisterPin5)
        pin.add(5,binding.inputRegisterPin6)
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
                    pin.get(i).setBackgroundResource(R.drawable.pin_input_background)

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

}
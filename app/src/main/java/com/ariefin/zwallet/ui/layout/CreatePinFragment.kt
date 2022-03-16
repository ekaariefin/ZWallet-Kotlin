package com.ariefin.zwallet.ui.layout

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
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentCreatePinBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.CreatePinRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.ui.widget.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class CreatePinFragment : Fragment() {
    private lateinit var binding: FragmentCreatePinBinding
    private lateinit var prefs: SharedPreferences
    var pin  = mutableListOf<EditText>()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditText()

        binding.btnConfirmCreatePin.setOnClickListener() {
            val createpin = CreatePinRequest(
                getpin().toString()
            )

            NetworkConfig(context).buildApi().createPIN(createpin)
                .enqueue(object : Callback<APIResponse<User>> {
                    override fun onResponse(
                        call: Call<APIResponse<User>>,
                        response: Response<APIResponse<User>>
                    ) {
                        if(response.body()?.status != HttpsURLConnection.HTTP_OK){
                            Toast.makeText(context, "PIN Configuration Failed", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, "Proses Konfigurasi PIN telah berhasil", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.createPinSuccessFragment)
                        }
                    }

                    override fun onFailure(call: Call<APIResponse<User>>, t: Throwable) {
                        Toast.makeText(context, "Gagal saat memproses perubahan password", Toast.LENGTH_SHORT).show()
                    }
                })

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

}
package com.ariefin.zwallet.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.Navigation
import com.ariefin.zwallet.ui.home.MainActivity
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentRegisterBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.LoginRequest
import com.ariefin.zwallet.model.request.RegisterRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var prefs: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnRegister.setOnClickListener {
            if (binding.inputUsernameRegister.text.isNullOrEmpty() || binding.inputEmailRegister.text.isNullOrEmpty() || binding.inputPasswordRegister.text.isNullOrEmpty()){
                Toast.makeText(activity, "username or email or password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val registerRequest = RegisterRequest(
                binding.inputUsernameRegister.text.toString(),
                binding.inputEmailRegister.text.toString(),
                binding.inputPasswordRegister.text.toString()
            )
            NetworkConfig(context).getService().register(registerRequest)
                .enqueue(object : Callback<APIResponse<User>> {
                    override fun onResponse(
                        call: Call<APIResponse<User>>,
                        response: Response<APIResponse<User>>
                    ) {
                        if(response.body()?.status != HttpsURLConnection.HTTP_OK){
                            Toast.makeText(context, "Authentication failed: Wrong username/email/password", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val res = response.body()!!.message
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                            Handler().postDelayed({
                                val intent = Intent(activity, AuthActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }, 1000)
                        }
                    }

                    override fun onFailure(call: Call<APIResponse<User>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                })



        }
        binding.signInText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.registerActionLogin)
        }




    }


}
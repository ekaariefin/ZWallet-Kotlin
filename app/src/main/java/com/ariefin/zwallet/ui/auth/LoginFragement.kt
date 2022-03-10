package com.ariefin.zwallet.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentLoginBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.LoginRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.ui.home.MainActivity
import com.ariefin.zwallet.utils.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.HTTP
import javax.net.ssl.HttpsURLConnection
import androidx.core.widget.addTextChangedListener

class LoginFragement : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var prefs: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.inputPassword.addTextChangedListener {
            if (binding.inputPassword.text.length > 8) {
                binding.btnLogin.setBackgroundResource(R.drawable.background_primary)
                binding.btnLogin.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (binding.inputPassword.text.length <= 8) {
                binding.btnLogin.setBackgroundResource(R.drawable.background_button_grey)
                binding.btnLogin.setTextColor(Color.parseColor("#9DA6B5"))
            }
        }


        binding.btnLogin.setOnClickListener {
            if (binding.inputEmail.text.isNullOrEmpty() || binding.inputPassword.text.isNullOrEmpty()){
                Toast.makeText(activity, "email or password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val loginRequest = LoginRequest(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
            NetworkConfig(context).getService().login(loginRequest)
                .enqueue(object : Callback<APIResponse<User>> {
                    override fun onResponse(
                        call: Call<APIResponse<User>>,
                        response: Response<APIResponse<User>>
                    ) {
                        if(response.body()?.status != HttpsURLConnection.HTTP_OK){
                            Toast.makeText(context, "Authentication failed: Wrong email/password", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val res = response.body()!!.data

                            with (prefs.edit()){
                                putBoolean(KEY_LOGGED_IN, true)
                                putString(KEY_USER_EMAIL, res.email)
                                putString(KEY_USER_TOKEN, res.token)
                                putString(KEY_USER_REFRESH_TOKEN, res.refreshToken)
                                apply()
                            }
                            Handler().postDelayed({
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }, 2000)
                        }
                    }

                    override fun onFailure(call: Call<APIResponse<User>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.signUpText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.loginActionRegister)
        }

        binding.textForgotPassword.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.loginActionReset)
        }

    }

}
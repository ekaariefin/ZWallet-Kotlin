package com.ariefin.zwallet.ui.layout.auth.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentLoginBinding
import com.ariefin.zwallet.ui.layout.main.MainActivity
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class LoginFragement : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

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
            if (binding.inputEmail.text.isNullOrEmpty() || binding.inputPassword.text.isNullOrEmpty()) {
                Toast.makeText(activity, "email or password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val response = viewModel.login(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )

            response.observe(viewLifecycleOwner) {
                when (it.state) {
                    State.LOADING -> {
                        loadingDialog.start("Processing your request")
                    }
                    State.SUCCESS -> {
                        loadingDialog.dismiss()
                        if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                            with(preferences.edit()) {
                                putBoolean(KEY_LOGGED_IN, true)
                                putString(KEY_USER_EMAIL, it.resource.data?.email)
                                putString(KEY_USER_TOKEN, it.resource.data?.token)
                                putString(KEY_USER_REFRESH_TOKEN, it.resource.data?.refreshToken)
                                apply()
                            }

                            if(it.resource.data?.hasPin!!) {
                                Handler().postDelayed({
                                    val intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                }, 1000)
                            }
                            else {
                                Navigation.findNavController(view)
                                    .navigate(R.id.action_loginFragement_to_createPinFragment)
                            }
                        } else {
                            Toast.makeText(context, it.resource?.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                    State.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

        binding.signUpText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.loginActionRegister)
        }

        binding.textForgotPassword.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.loginActionReset)
        }

    }

}
package com.ariefin.zwallet.ui.layout.auth.register

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentRegisterBinding
import com.ariefin.zwallet.ui.layout.auth.AuthViewModel
import com.ariefin.zwallet.ui.layout.main.MainActivity
import com.ariefin.zwallet.ui.widget.LoadingDialog
import com.ariefin.zwallet.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        preferences = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)


        binding.btnRegister.setOnClickListener {
            if (binding.inputUsernameRegister.text.isNullOrEmpty() || binding.inputEmailRegister.text.isNullOrEmpty() || binding.inputPasswordRegister.text.isNullOrEmpty()){
                Toast.makeText(activity, "Seluruh data wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val response = viewModel.register(
                binding.inputUsernameRegister.text.toString(),
                binding.inputEmailRegister.text.toString(),
                binding.inputPasswordRegister.text.toString()
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
                                }, 1500)
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
        binding.signInText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.registerActionLogin)
        }
    }
}
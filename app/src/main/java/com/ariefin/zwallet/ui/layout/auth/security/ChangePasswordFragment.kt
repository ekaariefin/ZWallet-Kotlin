package com.ariefin.zwallet.ui.layout.auth.security

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentChangePasswordBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.ChangePasswordRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.ui.layout.SplashScreenActivity
import com.ariefin.zwallet.utils.KEY_LOGGED_IN
import com.ariefin.zwallet.utils.PREFS_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(view)
                .popBackStack()
        }

        binding.btnContinueChangePassword.setOnClickListener {
            if (binding.inputCurrentPassword.text.isNullOrEmpty() || binding.inputNewPassword.text.isNullOrEmpty() || binding.inputConfirmPassword.text.isNullOrEmpty()){
                Toast.makeText(activity, "old password or new password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.inputNewPassword.text.isNullOrEmpty() != binding.inputConfirmPassword.text.isNullOrEmpty()){
                Toast.makeText(activity, "Password do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val changePasswordRequest = ChangePasswordRequest(
                binding.inputCurrentPassword.text.toString(),
                binding.inputNewPassword.text.toString()
            )
            NetworkConfig(context).buildApi().changePassword(changePasswordRequest)
                .enqueue(object : Callback<APIResponse<User>> {
                    override fun onResponse(
                        call: Call<APIResponse<User>>,
                        response: Response<APIResponse<User>>
                    ) {
                        if(response.body()?.status != HttpsURLConnection.HTTP_OK){
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val res = response.body()!!.message
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.homeFragment2)
                        }
                    }

                    override fun onFailure(call: Call<APIResponse<User>>, t: Throwable) {
                        Toast.makeText(context, "Gagal saat memproses perubahan password", Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }


}
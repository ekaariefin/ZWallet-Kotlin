package com.ariefin.zwallet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.Navigation
import com.ariefin.zwallet.databinding.FragmentChangePasswordBinding
import com.ariefin.zwallet.databinding.FragmentRegisterBinding
import com.ariefin.zwallet.model.APIResponse
import com.ariefin.zwallet.model.User
import com.ariefin.zwallet.model.request.ChangePasswordRequest
import com.ariefin.zwallet.model.request.RegisterRequest
import com.ariefin.zwallet.network.NetworkConfig
import com.ariefin.zwallet.utils.PREFS_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        //your code here
    }

}

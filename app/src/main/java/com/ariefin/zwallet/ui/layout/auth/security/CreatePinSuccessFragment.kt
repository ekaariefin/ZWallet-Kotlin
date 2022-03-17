package com.ariefin.zwallet.ui.layout.auth.security

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentCreatePinSuccessBinding
import com.ariefin.zwallet.utils.PREFS_NAME


class CreatePinSuccessFragment : Fragment() {
    private lateinit var binding: FragmentCreatePinSuccessBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePinSuccessBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.btnConfirmToLogin.setOnClickListener() {
            Toast.makeText(context, "Mengalihkan...", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(view).navigate(R.id.action_createPinSuccessFragment_to_loginFragement)
        }


    }

}
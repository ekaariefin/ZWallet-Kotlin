package com.ariefin.zwallet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ariefin.zwallet.databinding.FragmentInputNewPinBinding
import com.ariefin.zwallet.databinding.FragmentInputOtpBinding
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.PREFS_NAME


class CreatePinSuccessFragment : Fragment() {
    private lateinit var binding: FragmentInputNewPinBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputNewPinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
    }

}
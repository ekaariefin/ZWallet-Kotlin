package com.ariefin.zwallet.ui.layout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.ariefin.zwallet.databinding.FragmentTransferBinding
import com.ariefin.zwallet.utils.PREFS_NAME

private const val ARG_NAME = "name"
private const val ARG_PHONENUM = "phone"

private const val TAG = "TransferFragment"
class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding
    private lateinit var prefs: SharedPreferences

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_NAME)
            param2 = it.getString(ARG_PHONENUM)
            Log.d(TAG, "onCreate: $param1")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        binding.apply {
            transferUserName.text = param1
            transferUserPhone.text = param2
        }

        binding.btnContinueToPin.setOnClickListener {

        }



    }

}

package com.ariefin.zwallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ariefin.zwallet.ui.home.MainActivity
import com.ariefin.zwallet.R
import com.ariefin.zwallet.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
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

        binding.btnLogin.setOnClickListener {
            Handler().postDelayed({
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }, 2000)
        }

        binding.signInText.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.registerActionLogin)
        }
    }


}
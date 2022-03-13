package com.ariefin.zwallet.ui.main.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import com.ariefin.zwallet.SplashScreenActivity
import com.ariefin.zwallet.databinding.FragmentUserBinding
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.KEY_LOGGED_IN
import com.ariefin.zwallet.utils.PREFS_NAME
import javax.net.ssl.HttpsURLConnection

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.textButtonPersonalInfo.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_userFragment_to_personalInfoFragment)
        }

        binding.textButtonChangePin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_userFragment_to_changePinFragment)
        }

        binding.textButtonChangePassword.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_userFragment_to_changePasswordFragment)
        }

        binding.textButtonLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    with(prefs.edit()) {
                        putBoolean(KEY_LOGGED_IN, false)
                        apply()
                    }
                    val intent = Intent(activity, SplashScreenActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
                .show()
        }

    }

    private fun prepareData() {

        viewModel.getBalance().observe(viewLifecycleOwner) {
            if(it.status == HttpsURLConnection.HTTP_OK) {
                binding.apply {
                    profileName.text = it.data?.get(0)?.name
                    profilePhoneNum.text = it.data?.get(0)?.phone
                }
            } else {
                Toast.makeText(context, "Terjadi Kesalahan Saat Memproses Data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



}
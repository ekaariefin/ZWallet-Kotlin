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
import com.ariefin.zwallet.databinding.FragmentHomeBinding
import com.ariefin.zwallet.databinding.FragmentUserBinding
import com.ariefin.zwallet.ui.main.home.HomeViewModel
import com.ariefin.zwallet.ui.viewModelsFactory
import com.ariefin.zwallet.utils.Helper.formatPrice
import com.ariefin.zwallet.utils.KEY_LOGGED_IN
import com.ariefin.zwallet.utils.PREFS_NAME
import com.ariefin.zwallet.utils.State
import com.ariefin.zwallet.widget.LoadingDialog
import javax.net.ssl.HttpsURLConnection

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var prefs: SharedPreferences
    private val viewModel: HomeViewModel by viewModelsFactory { HomeViewModel(requireActivity().application) }
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        prepareData()

        binding.textButtonPersonalInfo.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_userFragment_to_personalInfoFragment)
        }

        binding.textButtonChangePin.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_userFragment_to_changePinFragment)
        }

        binding.textButtonChangePassword.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_userFragment_to_changePasswordFragment)
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
            when (it.state) {
                State.LOADING -> {
                    loadingDialog.start("Processing your request")
                }

                State.SUCCESS -> {

                    if (it.resource?.status == HttpsURLConnection.HTTP_OK) {
                        binding.apply {
                            profileName.text = it.resource.data?.get(0)?.name
                            profilePhoneNum.text = it.resource.data?.get(0)?.phone
                        }
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    loadingDialog.dismiss()
                }

                State.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }


            }


        }


    }
}
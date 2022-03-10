package com.ariefin.zwallet.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariefin.zwallet.R
import com.ariefin.zwallet.SplashScreenActivity
import com.ariefin.zwallet.adapter.TransactionAdapter
import com.ariefin.zwallet.data.Transaction
import com.ariefin.zwallet.databinding.FragmentHomeBinding
import com.ariefin.zwallet.utils.KEY_LOGGED_IN
import com.ariefin.zwallet.utils.PREFS_NAME

class HomeFragment : Fragment() {
    private val transactionData = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        this.transactionAdapter = TransactionAdapter(transactionData)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerTransaction.layoutManager = layoutManager
        binding.recyclerTransaction.adapter = transactionAdapter
        prepareData()

        binding.profileImage.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment2_to_userFragment)
        }

        binding.notificationIcon.setOnClickListener {
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
        this.transactionData.add(Transaction(
            transactionImage = activity?.getDrawable(R.drawable.avatar3)!!,
            transactionName =  "Githa Refina",
            transactionNominal = 250000.00,
            transactionType = "Cash In"
        ))
        this.transactionData.add(Transaction(
            transactionImage = activity?.getDrawable(R.drawable.avatar2)!!,
            transactionName =  "Hatta Febriansyah",
            transactionNominal = 20000.00,
            transactionType = "Cash In"
        ))
        this.transactionData.add(Transaction(
            transactionImage = activity?.getDrawable(R.drawable.avatar1)!!,
            transactionName =  "Yudhi Nur Bayu",
            transactionNominal = 50000.00,
            transactionType = "Cash In"
        ))
        this.transactionData.add(Transaction(
            transactionImage = activity?.getDrawable(R.drawable.avatar4)!!,
            transactionName =  "Aftabudin Arsyad",
            transactionNominal = 70000.00,
            transactionType = "Cash In"
        ))
        this.transactionData.add(Transaction(
            transactionImage = activity?.getDrawable(R.drawable.avatar5)!!,
            transactionName =  "Efrinaldi Al Zuhri",
            transactionNominal = 90000.00,
            transactionType = "Cash In"
        ))
        this.transactionData.add(Transaction(
            transactionImage = activity?.getDrawable(R.drawable.avatar6)!!,
            transactionName =  "Klin Agusta",
            transactionNominal = 1200000.00,
            transactionType = "Cash In"
        ))
        this.transactionAdapter.notifyDataSetChanged()
    }

}
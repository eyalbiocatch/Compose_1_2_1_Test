package com.biocatch.compose1_2_1test.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.biocatch.compose1_2_1test.R
import com.biocatch.compose1_2_1test.SDKManager
import com.biocatch.compose1_2_1test.base.BaseFragment
import com.biocatch.compose1_2_1test.databinding.FragmentUserDashboardBinding

class FragmentUserDashboard : BaseFragment() {

    private lateinit var binding: FragmentUserDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<FragmentUserDashboardBinding>(inflater, R.layout.fragment_user_dashboard, container, false)

        // navigate to payments screen
        binding.paymentsBtn.setOnClickListener { findNavController().navigate(FragmentUserDashboardDirections.toPayments()) }

        // sign out manually
        binding.signOutBtn.setOnClickListener { findNavController().navigate(FragmentUserDashboardDirections.toLogout()) }

        // navigate back to the login screen
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(FragmentUserDashboardDirections.toLogout())
        }

        binding.root.setOnTouchListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        SDKManager.changeContext("Dashboard_Screen")
    }
}

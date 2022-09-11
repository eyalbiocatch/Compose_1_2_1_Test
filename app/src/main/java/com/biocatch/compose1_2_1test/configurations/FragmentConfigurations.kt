package com.biocatch.compose1_2_1test.configurations

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.biocatch.compose1_2_1test.R
import com.biocatch.compose1_2_1test.SDKManager
import com.biocatch.compose1_2_1test.databinding.FragmentConfigurationsBinding

class FragmentConfigurations : Fragment(), View.OnClickListener {

    companion object {
        const val SERVER_URL_ARGS = "serverURL"
        const val CUSTOMER_ID_ARGS = "customerID"
        const val TIMEOUT_INTERVAL = "timeoutInterval"
        const val SD_SERVER_URL_ARGS = "sdServerURL"
    }

    private lateinit var binding: FragmentConfigurationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentConfigurationsBinding>(inflater, R.layout.fragment_configurations, container, false)

        // Back button
        requireActivity().onBackPressedDispatcher.addCallback { findNavController().popBackStack() }

        // Load configurations saved in the shared prefs.
        val preferences = requireActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        val serverURL = preferences.getString(SERVER_URL_ARGS, "https://wup-4ff4f23f.eu.v2.we-stats.com")
        binding.serverUrlTextInput.setText(serverURL)

        val customerID = preferences.getString(CUSTOMER_ID_ARGS, "dummy")
        binding.customerIDTextInput.setText(customerID)

        val timeoutInterval = preferences.getLong(TIMEOUT_INTERVAL, 300L)
        binding.timeoutTextInput.setText(timeoutInterval.toString())

        val sdServerURL =
            preferences.getString(SD_SERVER_URL_ARGS, "https://sdserver.bc2.customers.biocatch.com")
        binding.sdServerUrlTextInput.setText(sdServerURL)

        // listen for changes
        binding.saveBtn.setOnClickListener(this)

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        SDKManager.changeContext("Configuration")
    }

    @SuppressLint("ApplySharedPref")
    override fun onClick(v: View) {
        if (binding.serverUrlTextInput.text.isNullOrEmpty()) {
            binding.serverUrlTextInput.error = "Value can't be empty"
            return
        }

        if (binding.customerIDTextInput.text.isNullOrEmpty()) {
            binding.customerIDTextInput.error = "Value can't be empty"
            return
        }

        if (binding.timeoutTextInput.text.isNullOrEmpty()) {
            binding.timeoutTextInput.error = "Value can't be empty"
            return
        }

        val preferences = requireActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        preferences.edit()
            .putString(SERVER_URL_ARGS, binding.serverUrlTextInput.text.toString())
            .putString(CUSTOMER_ID_ARGS, binding.customerIDTextInput.text.toString())
            .putLong(TIMEOUT_INTERVAL, binding.timeoutTextInput.text.toString().toLong())
            .putString(SD_SERVER_URL_ARGS, binding.sdServerUrlTextInput.text.toString())
            .commit()

        requireActivity().recreate()
        requireActivity().onBackPressed()
    }
}

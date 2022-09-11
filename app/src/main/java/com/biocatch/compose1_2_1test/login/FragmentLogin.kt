package com.biocatch.compose1_2_1test.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.biocatch.compose1_2_1test.R
import com.biocatch.compose1_2_1test.SDKManager
import com.biocatch.compose1_2_1test.databinding.FragmentLoginBinding
import com.biocatch.client.android.sdk.BuildConfig
import java.util.*


class FragmentLogin : Fragment(), TextWatcher {

    private lateinit var binding: FragmentLoginBinding

//    private lateinit var repository: SDServerRepository
//    private lateinit var viewModel: SDServerViewModel

    /**
     * In this sample app, every time the user is introduced with the login screen (this screen)
     * then we must update the SDK with a new CSID value so it will know that this is a new
     * session starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fromLogOut = FragmentLoginArgs.fromBundle(requireArguments()).fromLogOut
        // In the launch of the app we already provided the SDK with the CSID, so we don't need to provide it again.
        // This is why we have this flag here - because we do need to update the CSID after the user logs out and
        // see this login screen again.
        if (fromLogOut) {
            // CSID must me updated every time the user starts a secure flow in the app, like - log in,
            // or when the user choose to log out, or maybe he is logged out by a system time out.
            SDKManager.updateCsid(UUID.randomUUID().toString())
            Log.d(SDKManager.SDK_TAG, "Looks like we are after a log out screen - update the CSID")
        }
//        repository = SDServerRepository(getNetworkService(requireContext()))
//        viewModel = ViewModelProviders.of(this, SDServerViewModel.FACTORY(repository))
//            .get(SDServerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(SDKManager.SDK_TAG, "$inflater  ${R.layout.fragment_login}  $container")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.configurationBtn.setOnClickListener {
            findNavController().navigate(FragmentLoginDirections.toConfigurations())
        }

        binding.loginButton.setOnClickListener {
            // Simulate a long running action - like authenticating with server...
            binding.viewSwitcher.showNext()
            // Flush the data collected
            SDKManager.flush()
            binding.information.postDelayed({
                binding.information.text = getString(R.string.login_authenticated)
            }, 1000)
            // Once user is "authenticated" we move on to the dashboard.
            binding.information.postDelayed({
                val destination = FragmentLoginDirections.toDashboard()
                findNavController().navigate(destination)
            }, 2000)

//            sendDataToServer()
        }

        binding.version.text = "Version: ${BuildConfig.VERSION_NAME}"
        binding.sdkVersion.text = "SDK Version: ${SDKManager.getVersion()}"

//        setObservers()
        return binding.root
    }

//    private fun sendDataToServer() {
//        viewModel.sendApiCallData(
//            SendClientDataRequest(
//                "LOGIN",
//                "Android",
//                SDKManager.getCSId()!!,
//                SDKManager.getUUId(),
//                SDKManager.getCid()!!
//            )
//        )
//    }

//    private fun setObservers() {
//        viewModel.data.observe(viewLifecycleOwner
//        ) { t ->
//            if (t != null && t?.api_response?.bcStatus != null) {
//                showMessage(t.api_response.bcStatus)
//            }
//        }
//        viewModel.error.observe(viewLifecycleOwner) { t ->
//            if (t?.isNotEmpty() == true) {
//                showMessage(t)
//            }
//        }
//        viewModel.spinner.observe(viewLifecycleOwner
//        ) { t ->
//            if (t == true) {
//                // binding.spinner.visibility = View.VISIBLE
//            } else {
//                // binding.spinner.visibility = View.GONE
//            }
//        }
//    }

    private fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.usernameET.addTextChangedListener(this)
        binding.passwordET.addTextChangedListener(this)
    }


    override fun onResume() {
        super.onResume()
        // Changing the context should happen each time a user steps into
        // an interesting part of the app flow. If the user enters the login screen and then -> dashboard and then back -> login
        // we need to get the context for these 3 steps "UserLogin", "Dashboard", and back to "UserLogin".

        // In some apps - fragments are only added and not removed, thus onCreate() of a fragment might only be called once and our flow of
        // login -> dashboard -> login, will only change context twice:
        // "UserLogin" and "Dashboard".
        // That is why it's better to call change context in onResume(), so we always get the complete "flow".
        SDKManager.changeContext("UserLogin")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        SDKManager.flush()
    }

    override fun afterTextChanged(s: Editable?) {
    }
}
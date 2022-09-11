package com.biocatch.compose1_2_1test.payments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.biocatch.compose1_2_1test.SDKManager
import com.biocatch.compose1_2_1test.configurations.FragmentConfigurations
import com.biocatch.compose1_2_1test.R
import com.biocatch.android.jetpackcomposesdk.core.ComposeElementType
import com.biocatch.android.jetpackcomposesdk.core.bcTracker

class FragmentPayments : Fragment(), View.OnTouchListener {

    private var deadline: Long = 0
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var timeoutInterval = 300L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = requireActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        timeoutInterval = preferences.getLong(FragmentConfigurations.TIMEOUT_INTERVAL, timeoutInterval)
        deadline = SystemClock.elapsedRealtime() + (timeoutInterval * 1000)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.setOnTouchListener(this)
        view.setContent { PaymentScreen() }

        // navigate back to the dashboard screen
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(FragmentPaymentsDirections.toDashboard())
        }

//        setObservers()
        return view
    }

    @Composable
    fun PaymentScreen() {
        val context = LocalContext.current
        var phoneET = remember { mutableStateOf(TextFieldValue()) }
        val phoneETErrorState = remember { mutableStateOf(false) }
        var amountET = remember { mutableStateOf(TextFieldValue()) }
        val amountETErrorState = remember { mutableStateOf(false) }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // Text("Welcome to payment screen")

            Text(
                text = getString(R.string.welcome_to_payments_screen),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(20.dp))

            OutlinedTextField(
                value = phoneET.value,
                onValueChange = {
                    if (phoneETErrorState.value) {
                        phoneETErrorState.value = false
                    }
                    phoneET.value = it
                },
                isError = phoneETErrorState.value,
                modifier = Modifier.bcTracker(ComposeElementType.OutlinedTextField, "PhoneETId", phoneET.value.text).fillMaxWidth(),
                label = { Text(text = getString(R.string.to)) },
                placeholder = { Text(text = getString(R.string._1_999_999999))}
            )
            if (phoneETErrorState.value) {
                Text(text = "Required", color = Color.Red)
            }

            Spacer(modifier = Modifier.size(20.dp))

            OutlinedTextField(value = amountET.value,
                onValueChange = {
                    if (amountETErrorState.value) {
                        amountETErrorState.value = false
                    }
                    amountET.value = it
                },
                isError = amountETErrorState.value,
                modifier = Modifier.bcTracker(ComposeElementType.OutlinedTextField, "AmountETId", amountET.value.text).fillMaxWidth(),
                label = { Text(text = getString(R.string.amount)) },
                placeholder = { Text(text = getString(R.string._1000))}
            )

            Spacer(modifier = Modifier.size(30.dp))

            Button(
                onClick = {
                    when{
                        phoneET.value.text.isEmpty() -> {
                            phoneETErrorState.value = true
                        }
                        amountET.value.text.isEmpty() ->  {
                            amountETErrorState.value = true
                        }
                        else -> {
                            phoneETErrorState.value = false
                            amountETErrorState.value = false
                            Toast.makeText(context, "Payment sent successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                content = { Text(text = getString(R.string.send_amount), color = Color.White)},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.colorPrimary))
            )

        }
    }


    override fun onResume() {
        super.onResume()
        SDKManager.changeContext("Payments Screen")
        maybeLogOut()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        deadline = SystemClock.elapsedRealtime() + (timeoutInterval * 1000)
        return false
    }

    private fun maybeLogOut() {
        if (SystemClock.elapsedRealtime() >= deadline) {
            findNavController().navigate(R.id.global_action_to_timeout)
        } else {
            mainThreadHandler.postDelayed(this::maybeLogOut, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }
}
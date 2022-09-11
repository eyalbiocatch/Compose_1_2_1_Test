package com.biocatch.compose1_2_1test.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.biocatch.compose1_2_1test.R
import com.biocatch.compose1_2_1test.configurations.FragmentConfigurations

/**
 * A base fragment that implements a time out mechanism
 * to log a user out after a some interval.
 *
 * This class has no interesting code related to the SDK directly.
 */
open class BaseFragment : Fragment(), View.OnTouchListener {

    private var deadline: Long = 0
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var timeoutInterval = 300L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        val preferences = requireActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        timeoutInterval = preferences.getLong(FragmentConfigurations.TIMEOUT_INTERVAL, timeoutInterval)
        deadline = SystemClock.elapsedRealtime() + (timeoutInterval * 1000)
        return v
    }

    override fun onResume() {
        super.onResume()
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
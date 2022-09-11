package com.biocatch.compose1_2_1test.login

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.biocatch.compose1_2_1test.R
import com.biocatch.compose1_2_1test.databinding.FragmentTimedOutDialogBinding

/**
 *
 * Just a view for this sample app to ask the user if he wants to log out.
 * Implementation details are not important here.
 *
 */
class FragmentDialogTimedOut : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTimedOutDialogBinding>(inflater, R.layout.fragment_timed_out_dialog, container, false)
        binding.exitBtn.setOnClickListener {
            val directions = FragmentDialogTimedOutDirections.globalActionToLogin()
            directions.arguments.putBoolean("fromLogOut", true)
            findNavController().navigate(directions)
            dismiss()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (showsDialog) {
            val displayMetrics = DisplayMetrics()
            dialog?.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            dialog?.window?.setLayout(displayMetrics.widthPixels, displayMetrics.heightPixels / 3)
        }
    }
}

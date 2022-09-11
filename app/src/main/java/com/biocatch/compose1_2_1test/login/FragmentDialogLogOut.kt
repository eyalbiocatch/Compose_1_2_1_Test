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
import com.biocatch.compose1_2_1test.databinding.FragmentDialogLogOutBinding

/**
 *
 * Just a view for this sample app to ask the user if he wants to log out.
 * Implementation details are not important here.
 *
 */
class FragmentDialogLogOut : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentDialogLogOutBinding>(inflater, R.layout.fragment_dialog_log_out, container, false)
        binding.yesBtn.setOnClickListener {
            val directions = FragmentDialogLogOutDirections.globalActionToLogin()
            directions.arguments.putBoolean("fromLogOut", true)
            findNavController().navigate(directions)
            dismiss()
        }
        binding.noBtn.setOnClickListener {
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

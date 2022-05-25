package com.aita.base.bottomsheets

import android.app.Dialog
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AbsBackAwareBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var fallback: (() -> Unit)? = null

    protected val onBackPressedDispatcher: OnBackPressedDispatcher =
        OnBackPressedDispatcher { fallback?.invoke() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireContext(), theme) {
            init {
                fallback = { super.onBackPressed() }
            }

            override fun onBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }
}

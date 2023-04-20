package com.example.stegomessenger.arch.infra

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class AbsFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    abstract fun getBackStackTag(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        activity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (parentFragmentManager.backStackEntryCount <= 1) {
                        activity.finish()
                    } else {
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        )
    }
}

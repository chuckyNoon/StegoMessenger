package com.example.diplomclient.arch.infra

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aita.arch.di.regular.AppDepsProvider
import com.example.diplomclient.arch.flux.util.AppViewModelFactory
import com.example.diplomclient.main.AppViewModel

abstract class AbsFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    val appViewModelFactory: AppViewModelFactory by lazy {
        val activity = requireActivity()
        val activityViewModelProvider = ViewModelProvider(activity)
        val appViewModel = activityViewModelProvider.get(AppViewModel::class.java)
        return@lazy appViewModel.appViewModelFactory
    }

    val appDepsProvider: AppDepsProvider by lazy {
        val activity = requireActivity()
        val activityViewModelProvider = ViewModelProvider(activity)
        val appViewModel = activityViewModelProvider.get(AppViewModel::class.java)
        return@lazy appViewModel.appDepsProvider
    }

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

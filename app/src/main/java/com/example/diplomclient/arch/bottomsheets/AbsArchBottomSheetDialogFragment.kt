package com.example.diplomclient.arch.bottomsheets

import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.arch.flux.util.AppViewModelFactory
import com.example.diplomclient.main.AppViewModel

abstract class AbsArchBottomSheetDialogFragment : AbsBottomSheetDialogFragment {

    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected val appViewModelFactory: AppViewModelFactory by lazy {
        val activity = requireActivity()
        val activityViewModelProvider = ViewModelProvider(activity)
        val appViewModel = activityViewModelProvider.get(AppViewModel::class.java)
        return@lazy appViewModel.appViewModelFactory
    }
}

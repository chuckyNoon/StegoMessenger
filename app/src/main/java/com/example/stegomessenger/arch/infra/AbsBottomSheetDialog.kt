package com.example.stegomessenger.arch.infra

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ref.WeakReference

abstract class AbsBottomSheetDialog() : BottomSheetDialogFragment() {

    private var fallback: (() -> Unit)? = null

    private val onBackPressedDispatcher: OnBackPressedDispatcher =
        OnBackPressedDispatcher { fallback?.invoke() }

    @LayoutRes
    private var contentLayoutId = 0

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : this() {
        this.contentLayoutId = contentLayoutId
    }

    fun showAfterKeyboardHides(
        fragmentManager: FragmentManager,
        tag: String?,
        activity: Activity?
    ) {
        if (activity == null) {
            super.show(fragmentManager, tag)
            return
        }
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView == null) {
            super.show(fragmentManager, tag)
            return
        }
        val windowToken = currentFocusedView.windowToken
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val hide = inputMethodManager.hideSoftInputFromWindow(
            windowToken,
            0,
            WeakResultReceiver(currentFocusedView.handler, this, fragmentManager, tag)
        )
        if (!hide) {
            super.show(fragmentManager, tag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.example.stegomessenger.R.style.DefaultBottomSheetTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireContext(), theme) {
            init {
                fallback = { super.onBackPressed() }
            }

            override fun onBackPressed() {
                onBackPressedDispatcher.onBackPressed()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        if (contentLayoutId != 0) {
            inflater.inflate(contentLayoutId, container, false)
        } else {
            null
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener { dismiss() }
    }

    private fun showInternal(fragmentManager: FragmentManager, tag: String?) {
        super.show(fragmentManager, tag)
    }

    private class WeakResultReceiver constructor(
        handler: Handler?,
        fragment: AbsBottomSheetDialog,
        fragmentManager: FragmentManager,
        tag: String?
    ) : ResultReceiver(handler) {
        private val fragmentWeakReference: WeakReference<AbsBottomSheetDialog>
        private val fragmentManagerWeakReference: WeakReference<FragmentManager>
        private val tag: String?

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            val fragment = fragmentWeakReference.get() ?: return
            val fragmentManager = fragmentManagerWeakReference.get() ?: return
            fragment.showInternal(fragmentManager, tag)
        }

        init {
            fragmentWeakReference = WeakReference(fragment)
            fragmentManagerWeakReference = WeakReference(fragmentManager)
            this.tag = tag
        }
    }

}
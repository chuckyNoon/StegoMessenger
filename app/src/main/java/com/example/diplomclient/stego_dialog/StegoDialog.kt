package com.example.diplomclient.stego_dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.aita.arch.dispatcher.Dispatchable
import com.example.diplomclient.R
import com.example.diplomclient.arch.flux.util.AppViewModelFactory
import com.example.diplomclient.chat.ChatFragment
import com.example.diplomclient.chat.getPicassoInstance
import com.example.diplomclient.common.PickImageRequest
import com.example.diplomclient.main.AppViewModel

class StegoDialog() : DialogFragment() {

    @LayoutRes
    private var contentId = 0

    private var dispatchable: Dispatchable? = null

    private val appViewModelFactory: AppViewModelFactory by lazy {
        val activity = requireActivity()
        val activityViewModelProvider = ViewModelProvider(activity)
        val appViewModel = activityViewModelProvider.get(AppViewModel::class.java)
        return@lazy appViewModel.appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DefaultDialogFragmentTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View =
            inflater.inflate(R.layout.dialog_stego, container, false)
        val contentContainer = rootView.findViewById<FrameLayout>(R.id.content)
        if (contentId != 0) {
            contentContainer.addView(inflater.inflate(contentId, null))
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(StegoViewModel::class.java)
        val activity = requireActivity()
        val parentFragment = requireParentFragment()
        val requestManager = getPicassoInstance(this)

        val selectImageButton = view.findViewById<Button>(R.id.select_image).apply {
            setOnClickListener {
                PickImageRequest(ChatFragment.REQUEST_CODE).start(parentFragment)
            }
        }
        val selectContainerButton = view.findViewById<Button>(R.id.select_container).apply {
            setOnClickListener {
                PickImageRequest(ChatFragment.CONTAINER_REQUEST_CODE).start(parentFragment)
            }
        }
        val sendButton = view.findViewById<Button>(R.id.send).apply {
            setOnClickListener {
                viewModel.dispatch(StegoAction.ClickSend(activity.contentResolver))
            }
        }
        val displayImageView = view.findViewById<ImageView>(R.id.display_iv)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: StegoViewState? ->
            viewState ?: return@observe

            sendButton.isEnabled = viewState.isSendButtonAvailable
            selectImageButton.text = viewState.imageButtonText
            selectContainerButton.text = viewState.containerButtonText
            if (viewState.displayBitmap != null) {
                requestManager.load(viewState.displayBitmap).into(displayImageView)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        val dialog: Dialog = requireDialog()
        val view: View = requireView()
        val window = dialog.window

        if (window != null) {
            val params: ViewGroup.LayoutParams = window.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        view.setOnTouchListener { v: View, event: MotionEvent ->
            val action = event.action and MotionEvent.ACTION_MASK
            if (action == MotionEvent.ACTION_UP && v.id == R.id.root) {
                dialog.cancel()
                return@setOnTouchListener false
            }
            true
        }
    }
}

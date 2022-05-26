package com.example.diplomclient.stego_dialog

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.diplomclient.R
import com.example.diplomclient.arch.bottomsheets.AbsArchBottomSheetDialogFragment
import com.example.diplomclient.chat.ChatFragment
import com.example.diplomclient.chat.getPicassoInstance
import com.example.diplomclient.common.PickImageRequest
import com.example.diplomclient.common.view.MyCheckBox

class StegoDialog : AbsArchBottomSheetDialogFragment(R.layout.dialog_stego) {

    override fun onStart() {
        super.onStart()

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(StegoViewModel::class.java)
        val activity = requireActivity()
        val view = requireView()
        val context = requireContext()
        val parentFragment = requireParentFragment()
        val requestManager = getPicassoInstance(this)

        val titleTextView = view.findViewById<TextView>(R.id.title_tv)
        val checkBox = view.findViewById<MyCheckBox>(R.id.checkbox).apply {
            onClick = {
                viewModel.dispatch(StegoAction.ClickCheckBox)
            }
            setIsChecked(requestManager, false)
            setText("Protect with steganography")
        }
        val stegoContainer = view.findViewById<View>(R.id.stego_container_block)
        val addStegoContainer = view.findViewById<View>(R.id.add_container_block).apply {
            setOnClickListener {
                PickImageRequest(ChatFragment.CONTAINER_REQUEST_CODE).start(parentFragment)
            }
        }
        val addedStegoImageView = view.findViewById<ImageView>(R.id.added_container_iv).apply {
            setOnClickListener {
                PickImageRequest(ChatFragment.CONTAINER_REQUEST_CODE).start(parentFragment)
            }
        }
        val contentEditText = view.findViewById<EditText>(R.id.content_et)

        val sendButton = view.findViewById<View>(R.id.send_btn).apply {
            setOnClickListener {
                viewModel.dispatch(StegoAction.ClickSend(activity.contentResolver))
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: StegoViewState? ->
            viewState ?: return@observe

            titleTextView.text = viewState.titleText
            checkBox.setIsChecked(requestManager, viewState.isStegoCheckBoxSelected)
            if (viewState.isStegoCheckBoxSelected) {
                stegoContainer.visibility = View.VISIBLE
                if (viewState.containerBitmapUriStr.isNullOrEmpty()) {
                    addedStegoImageView.visibility = View.GONE
                    addStegoContainer.visibility = View.VISIBLE
                } else {
                    addedStegoImageView.visibility = View.VISIBLE
                    addStegoContainer.visibility = View.GONE
                    requestManager
                        .load(viewState.containerBitmapUriStr)
                        .transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundedCorners(
                                    context.resources.getDimensionPixelSize(R.dimen.image_cornder_radius)
                                )
                            )
                        )
                        .into(addedStegoImageView)
                }
            } else {
                stegoContainer.visibility = View.GONE
            }

            sendButton.isEnabled = viewState.isSendButtonEnabled

            when (viewState) {
                is StegoViewState.Image -> {
                    contentEditText.visibility = View.GONE
                }
                is StegoViewState.Text -> {
                    contentEditText.visibility = View.VISIBLE
                    if (contentEditText.text.toString().isEmpty()) {
                        contentEditText.setText(viewState.contentText)
                    }
                }
            }
        }

        viewModel.closeLiveData.observe(viewLifecycleOwner) { unit: Unit? ->
            unit ?: return@observe
            dismiss()
        }
    }

    override fun getRequestCode(): Int = 0

    override fun isExpandFull(): Boolean = true
}

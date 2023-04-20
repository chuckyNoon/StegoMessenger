package com.example.stegomessenger.stego_dialog

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.infra.AbsBottomSheetDialog
import com.example.stegomessenger.chat.ChatFragment
import com.example.stegomessenger.common.imageChooserIntent
import com.example.stegomessenger.common.view.MyCheckBox
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class StegoDialog : AbsBottomSheetDialog(R.layout.dialog_stego) {
    private val viewModel: StegoViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onStart() {
        super.onStart()

        val activity = requireActivity()
        val view = requireView()
        val context = requireContext()

        val titleTextView = view.findViewById<TextView>(R.id.title_tv)
        val checkBox = view.findViewById<MyCheckBox>(R.id.checkbox).apply {
            onClick = {
                viewModel.dispatch(StegoAction.ClickCheckBox)
            }
            setIsChecked(requestManager, false)
            setText(getString(R.string.secure_using_staganography))
        }
        val stegoContainer = view.findViewById<View>(R.id.stego_container_block)
        val addStegoContainer = view.findViewById<View>(R.id.add_container_block).apply {
            setOnClickListener {
                activity.startActivityForResult(
                    imageChooserIntent(),
                    ChatFragment.CONTAINER_REQUEST_CODE
                )
            }
        }
        val addedStegoImageView = view.findViewById<ImageView>(R.id.added_container_iv).apply {
            setOnClickListener {
                activity.startActivityForResult(
                    imageChooserIntent(),
                    ChatFragment.CONTAINER_REQUEST_CODE
                )
            }
        }
        // text state only. start
        val contentEditText = view.findViewById<EditText>(R.id.content_et).apply {
            addTextChangedListener {
                val editable = it ?: return@addTextChangedListener
                val text = editable.toString()
                viewModel.dispatch(StegoAction.HandleContentTextChanged(text))
            }
        }
        // text state only. end

        val sendButton = view.findViewById<View>(R.id.send_btn).apply {
            setOnClickListener {
                viewModel.dispatch(StegoAction.ClickSend(activity.contentResolver))
            }
        }
        val progressBar = view.findViewById<ProgressBar>(R.id.pb)
        val dialogContent = view.findViewById<View>(R.id.dialog_content)

        // image state only. start
        val stegoContent = view.findViewById<View>(R.id.stego_content_block)
        val addedContentImageView = view.findViewById<ImageView>(R.id.added_content_iv).apply {
            setOnClickListener {
                activity.startActivityForResult(
                    imageChooserIntent(),
                    ChatFragment.CONTENT_REQUEST_CODE
                )
            }
        }
        val addContent = view.findViewById<View>(R.id.add_content_block).apply {
            setOnClickListener {
                activity.startActivityForResult(
                    imageChooserIntent(),
                    ChatFragment.CONTENT_REQUEST_CODE
                )
            }
        }
        // image state only. end

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
                                    context.resources.getDimensionPixelSize(R.dimen.send_image_corner_radius)
                                )
                            )
                        )
                        .into(addedStegoImageView)
                }
            } else {
                stegoContainer.visibility = View.GONE
            }

            if (viewState.isProgressBarVisible) {
                progressBar.visibility = View.VISIBLE
                dialogContent.visibility = View.INVISIBLE
            } else {
                dialogContent.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            sendButton.isEnabled = true // viewState.isSendButtonEnabled

            when (viewState) {
                is StegoViewState.Image -> {
                    contentEditText.visibility = View.GONE
                    stegoContent.visibility = View.VISIBLE

                    if (viewState.contentBitmapUriStr.isNullOrEmpty()) {
                        addedContentImageView.visibility = View.GONE
                        addContent.visibility = View.VISIBLE
                    } else {
                        addedContentImageView.visibility = View.VISIBLE
                        addContent.visibility = View.GONE

                        requestManager
                            .load(viewState.contentBitmapUriStr)
                            .transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(
                                        context.resources.getDimensionPixelSize(R.dimen.send_image_corner_radius)
                                    )
                                )
                            )
                            .into(addedContentImageView)
                    }
                }
                is StegoViewState.Text -> {
                    stegoContent.visibility = View.GONE

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
}

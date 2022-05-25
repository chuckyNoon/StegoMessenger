package com.example.diplomclient.stego_dialog

import androidx.lifecycle.ViewModelProvider
import com.example.diplomclient.R
import com.example.diplomclient.arch.bottomsheets.AbsArchBottomSheetDialogFragment
import com.example.diplomclient.chat.getPicassoInstance
import com.example.diplomclient.common.view.MyCheckBox

class StegoDialog : AbsArchBottomSheetDialogFragment(R.layout.dialog_stego) {

    override fun onStart() {
        super.onStart()

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(StegoViewModel::class.java)
        val activity = requireActivity()
        val view = requireView()
        val parentFragment = requireParentFragment()
        val requestManager = getPicassoInstance(this)

        val checkBox = view.findViewById<MyCheckBox>(R.id.checkbox).apply {
            onClick = {
                viewModel.dispatch(StegoAction.ClickCheckBox)
            }
            setIsChecked(requestManager, true)
            setText("Protect with steganography")
        }

        /*val selectImageButton = view.findViewById<Button>(R.id.select_image).apply {
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
        val contentBlock = view.findViewById<View>(R.id.content_block)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_block)

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: StegoViewState? ->
            viewState ?: return@observe

            sendButton.isEnabled = viewState.isSendButtonAvailable
            selectImageButton.text = viewState.imageButtonText
            selectContainerButton.text = viewState.containerButtonText
            if (viewState.displayBitmap != null) {
                requestManager.load(viewState.displayBitmap).into(displayImageView)
            }
            if (viewState.isInPgoress) {
                contentBlock.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            } else {
                contentBlock.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }

        viewModel.closeLiveData.observe(viewLifecycleOwner) { unit: Unit? ->
            unit ?: return@observe
            dismiss()
        }*/
    }

    override fun getRequestCode(): Int = 0

    override fun isExpandFull(): Boolean = true
}

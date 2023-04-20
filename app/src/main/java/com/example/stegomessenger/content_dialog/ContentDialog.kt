package com.example.stegomessenger.content_dialog

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.arch.infra.AbsBottomSheetDialog
import com.example.stegomessenger.chat.getPicassoInstance
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.chat.items.ImageMessageDelegate
import com.example.stegomessenger.chat.items.TextMessageAdapterDelegate
import com.example.stegomessenger.common.InsetSide
import com.example.stegomessenger.common.handleInsetsWithPaddingForSides

class ContentDialog : AbsBottomSheetDialog(R.layout.dialog_content) {

    override fun onStart() {
        super.onStart()

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(ContentViewModel::class.java)
        val view = requireView()
        val context = requireContext()
        val requestManager = getPicassoInstance(this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            handleInsetsWithPaddingForSides(
                InsetSide.START,
                InsetSide.END,
                InsetSide.BOTTOM
            )
        }
        val downloadButton = view.findViewById<View>(R.id.download_btn).apply {
            setOnClickListener {
                // TODO: implement
            }
        }

        val delegates = listOf(
            TextMessageAdapterDelegate(
                inflater = layoutInflater
            ),
            ImageMessageDelegate(
                inflater = layoutInflater,
                requestManager = requestManager,
                onImageClick = {
                    // Nothing to do here
                }
            )
        )

        val adapter = ComposableListAdapter(delegates).apply {
            recyclerView.adapter = this
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ContentViewState? ->
            viewState ?: return@observe

            adapter.submitList(viewState.cells)

            if (viewState.isDownloadButtonVisible) {
                downloadButton.visibility = View.VISIBLE
            } else {
                downloadButton.visibility = View.GONE
            }
        }
    }
}

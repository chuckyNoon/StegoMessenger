package com.example.diplomclient.content_dialog

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomclient.R
import com.example.diplomclient.arch.adapter.ComposableListAdapter
import com.example.diplomclient.arch.infra.AbsBottomSheetDialog
import com.example.diplomclient.chat.getPicassoInstance
import com.example.diplomclient.chat.items.ImageMessageCell
import com.example.diplomclient.chat.items.ImageMessageDelegate
import com.example.diplomclient.chat.items.TextMessageAdapterDelegate
import com.example.diplomclient.common.InsetSide
import com.example.diplomclient.common.handleInsetsWithPaddingForSides

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
            ) ,
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

            val cell = viewState.cells.firstOrNull()
            adapter.submitList(viewState.cells)

            if (cell is ImageMessageCell) {
                downloadButton.visibility = View.VISIBLE
            } else {
                downloadButton.visibility = View.GONE
            }
        }
    }
}

package com.example.diplomclient.content_dialog

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aita.adapter.composable.ComposableListAdapter
import com.aita.base.bottomsheets.ScrollBottomSheetHelper
import com.example.diplomclient.R
import com.example.diplomclient.arch.bottomsheets.AbsArchBottomSheetDialogFragment
import com.example.diplomclient.chat.getPicassoInstance
import com.example.diplomclient.chat.items.ImageMessageCell
import com.example.diplomclient.chat.items.ImageMessageDelegate
import com.example.diplomclient.common.InsetSide
import com.example.diplomclient.common.ViewUtils
import com.example.diplomclient.overview.model.TextMessageAdapterDelegate

class ContentDialog : AbsArchBottomSheetDialogFragment(R.layout.dialog_content) {

    override fun onStart() {
        super.onStart()

        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(ContentViewModel::class.java)
        val activity = requireActivity()
        val view = requireView()
        val context = requireContext()
        val parentFragment = requireParentFragment()
        val requestManager = getPicassoInstance(this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.START,
                InsetSide.END,
                InsetSide.BOTTOM
            )
        }
        val downloadButton = view.findViewById<View>(R.id.download_btn).apply {
            setOnClickListener {
            }
        }

        ScrollBottomSheetHelper(
            recyclerView,
            view.findViewById(R.id.button_block),
            view.findViewById(R.id.title_block),
        )

        val delegates = listOf(
            TextMessageAdapterDelegate(
                inflater = layoutInflater,
                requestManager = requestManager,
                onImageClick = {}
            ),
            ImageMessageDelegate(
                inflater = layoutInflater,
                requestManager = requestManager,
                onImageClick = {
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

    override fun getRequestCode(): Int = 0

    override fun isExpandFull(): Boolean = true
}

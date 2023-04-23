package com.example.stegomessenger.features.content_dialog

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.arch.infra.AbsBottomSheetDialog
import com.example.stegomessenger.common.InsetSide
import com.example.stegomessenger.common.handleInsetsWithPaddingForSides
import com.example.stegomessenger.features.chat.items.ImageMessageDelegate
import com.example.stegomessenger.features.chat.items.TextMessageAdapterDelegate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContentDialog : AbsBottomSheetDialog(R.layout.dialog_content) {
    private val viewModel: ContentViewModel by viewModels()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onStart() {
        super.onStart()

        val view = requireView()
        val context = requireContext()

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

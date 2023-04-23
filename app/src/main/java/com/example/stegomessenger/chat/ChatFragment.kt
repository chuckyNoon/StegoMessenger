package com.example.stegomessenger.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.common.hideKeyboard
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.chat.items.ImageMessageDelegate
import com.example.stegomessenger.chat.items.TextMessageAdapterDelegate
import com.example.stegomessenger.common.InsetSide
import com.example.stegomessenger.common.handleInsetsWithPaddingForSides
import com.example.stegomessenger.overview.items.DividerAdapterDelegate
import com.example.stegomessenger.stego_dialog.StegoAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : AbsFragment(R.layout.fragment_chat) {

    private val viewModel: ChatViewModel by viewModels()
    @Inject
    lateinit var requestManager: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            handleInsetsWithPaddingForSides(
                InsetSide.END,
                InsetSide.START,
            )
        }
        val chatNameTextView = view.findViewById<TextView>(R.id.chat_name_tv)

        view.findViewById<View>(R.id.toolbar).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }

        view.findViewById<View>(R.id.type_block).apply {
            handleInsetsWithPaddingForSides(
                InsetSide.BOTTOM,
                InsetSide.START,
                InsetSide.END,
            )
        }

        view.findViewById<View>(R.id.send_text_btn).apply {
            setOnClickListener {
                viewModel.dispatch(ChatAction.ClickSendText)
            }
        }

        view.findViewById<View>(R.id.send_img_btn).apply {
            setOnClickListener {
                viewModel.dispatch(ChatAction.ClickSendImage)
            }
        }

        val delegates = listOf(
            TextMessageAdapterDelegate(
                layoutInflater
            ),
            ImageMessageDelegate(
                layoutInflater,
                requestManager = requestManager,
                onImageClick = { cell: ImageMessageCell ->
                    viewModel.dispatch(ChatAction.ClickImage(cell))
                }
            ),
            DividerAdapterDelegate(layoutInflater)
        )

        val adapter = ComposableListAdapter(delegates).apply {
            recyclerView.adapter = this
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ChatViewState? ->
            viewState ?: return@observe

            chatNameTextView.text = viewState.chatName
            adapter.submitList(viewState.cells)
        }

        viewModel.completeLiveData.observe(viewLifecycleOwner) { unit: Unit? ->
            unit ?: return@observe

            this.hideKeyboard()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CONTENT_REQUEST_CODE -> {
                val imageUri = data?.data ?: return

                viewModel.dispatch(StegoAction.HandleContentImagePicked(imageUri.toString()))
            }
            CONTAINER_REQUEST_CODE -> {
                val imageUri = data?.data ?: return

                viewModel.dispatch(StegoAction.HandleContainerPicked(imageUri.toString()))
            }
        }
    }

    override fun getBackStackTag(): String = "ChatFragment"

    companion object {
        const val CONTENT_REQUEST_CODE = 1299
        const val CONTAINER_REQUEST_CODE = 1300
    }
}

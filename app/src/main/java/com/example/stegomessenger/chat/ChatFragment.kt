package com.example.stegomessenger.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.stegomessenger.R
import com.example.stegomessenger.arch.adapter.ComposableListAdapter
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.infra.AbsFragment
import com.example.stegomessenger.common.hideKeyboard
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.chat.items.ImageMessageDelegate
import com.example.stegomessenger.chat.items.TextMessageAdapterDelegate
import com.example.stegomessenger.common.InsetSide
import com.example.stegomessenger.common.handleInsetsWithPaddingForSides
import com.example.stegomessenger.main.MainApplication
import com.example.stegomessenger.overview.model.DividerAdapterDelegate
import com.example.stegomessenger.stego_dialog.StegoAction

class ChatFragment : AbsFragment(R.layout.fragment_chat) {

    private var dispatchable: Dispatchable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(ChatViewModel::class.java)

        this.dispatchable = viewModel

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

        val requestManager = getPicassoInstance(this)

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

        if (requestCode == CONTENT_REQUEST_CODE) {
            val dispatchable = dispatchable ?: return
            val imageUri = data?.data ?: return

            dispatchable.dispatch(
                StegoAction.HandleContentImagePicked(imageUri.toString())
            )
        } else if (requestCode == CONTAINER_REQUEST_CODE) {
            val dispatchable = dispatchable ?: return
            val imageUri = data?.data ?: return

            dispatchable.dispatch(
                StegoAction.HandleContainerPicked(imageUri.toString())
            )
        }
    }

    override fun getBackStackTag(): String = "ChatFragment"

    companion object {
        const val CONTENT_REQUEST_CODE = 1299
        const val CONTAINER_REQUEST_CODE = 1300
    }
}

// TODO: move to separate class
fun getPicassoInstance(fragment: Fragment): RequestManager {
    return if (fragment.isAdded && fragment.activity != null) {
        Glide.with(fragment)
    } else {
        Glide.with(MainApplication.getInstance())
    }
}

package com.example.diplomclient.chat

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aita.adapter.composable.ComposableListAdapter
import com.aita.arch.dispatcher.Dispatchable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.diplomclient.R
import com.example.diplomclient.arch.infra.AbsFragment
import com.example.diplomclient.arch.util.hideKeyboard
import com.example.diplomclient.common.InsetSide
import com.example.diplomclient.common.PickImageRequest
import com.example.diplomclient.common.ViewUtils
import com.example.diplomclient.common.launchBackgroundWork
import com.example.diplomclient.main.MainApplication
import com.example.diplomclient.overview.model.DividerAdapterDelegate
import com.example.diplomclient.overview.model.MessageAdapterDelegate

class ChatFragment : AbsFragment(R.layout.fragment_chat) {

    private var dispatchble: Dispatchable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        val viewModelProvider = ViewModelProvider(this, appViewModelFactory)
        val viewModel = viewModelProvider.get(ChatViewModel::class.java)

        this.dispatchble = viewModel

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.END,
                InsetSide.START,
            )
        }
        val chatNameTextView = view.findViewById<TextView>(R.id.chat_name_tv)
        val messageEditText = view.findViewById<EditText>(R.id.message_et).apply {
            addTextChangedListener {
                val text = it?.toString()
                if (!text.isNullOrEmpty()) {
                    viewModel.dispatch(ChatAction.TextTyped(it.toString()))
                }
            }
        }
        val sendButton = view.findViewById<Button>(R.id.send_btn).apply {
            setOnClickListener {
                viewModel.dispatch(ChatAction.ClickSend)
            }
        }
        val attachButton = view.findViewById<Button>(R.id.attach_btn).apply {
            setOnClickListener {
                PickImageRequest(REQUEST_CODE).start(this@ChatFragment)
            }
        }
        val typeBlock = view.findViewById<View>(R.id.type_block).apply {
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.BOTTOM,
                InsetSide.START,
                InsetSide.END
            )
        }
        val toolbar = view.findViewById<View>(R.id.toolbar).apply {
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }

        val delegates = listOf(
            MessageAdapterDelegate(layoutInflater, requestManager = getPicassoInstance(this)),
            DividerAdapterDelegate(layoutInflater)
        )

        val adapter = ComposableListAdapter(delegates).apply {
            recyclerView.adapter = this
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ChatViewState? ->
            viewState ?: return@observe

            chatNameTextView.text = viewState.chatName

            if (messageEditText.text.toString().isNullOrEmpty()) {
                messageEditText.setText(viewState.typedText)
            }

            adapter.submitList(viewState.cells)
        }

        viewModel.completeLiveData.observe(viewLifecycleOwner) { unit: Unit? ->
            unit ?: return@observe

            this.hideKeyboard()
            messageEditText.setText("")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val dispatchable = dispatchble ?: return
            val imageUri = data?.data ?: return

            launchBackgroundWork {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)

                dispatchable.dispatch(
                    ChatAction.FilePicked(bitmap = bitmap)
                )
            }
        }
    }

    override fun getBackStackTag(): String = "ChatFragment"

    companion object {
        private const val REQUEST_CODE = 1299
    }
}

fun getPicassoInstance(fragment: Fragment): RequestManager {
    return if (fragment.isAdded && fragment.activity != null) {
        Glide.with(fragment)
    } else {
        Glide.with(MainApplication.getInstance())
    }
}

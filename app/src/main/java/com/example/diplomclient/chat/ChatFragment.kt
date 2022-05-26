package com.example.diplomclient.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.example.diplomclient.common.ViewUtils
import com.example.diplomclient.main.MainApplication
import com.example.diplomclient.overview.model.DividerAdapterDelegate
import com.example.diplomclient.overview.model.TextMessageAdapterDelegate
import com.example.diplomclient.overview.model.TextMessageCell
import com.example.diplomclient.stego_dialog.StegoAction
import com.example.diplomclient.stego_dialog.StegoDialog

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

        val toolbar = view.findViewById<View>(R.id.toolbar).apply {
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.TOP,
                InsetSide.START,
                InsetSide.END
            )
        }

        view.findViewById<View>(R.id.type_block).apply {
            ViewUtils.handleInsetsWithPaddingForSides(
                this,
                InsetSide.BOTTOM,
                InsetSide.START,
                InsetSide.END,
            )
        }

        view.findViewById<View>(R.id.send_text_btn).apply {
            setOnClickListener {
                viewModel.dispatch(ChatAction.ClickSendText)
                StegoDialog().show(childFragmentManager, "f")
            }
        }

        val delegates = listOf(
            TextMessageAdapterDelegate(
                layoutInflater,
                requestManager = getPicassoInstance(this),
                onImageClick = { cell: TextMessageCell ->
                    /* AppLogger.log("1")
                     launchBackgroundWork {
                         AppLogger.log("2")
                         val hiddenBitmap = Algorithm().lsbDecode(cell.image!!)
                         AppLogger.log("3")
                         viewModel.dispatch(CoreAction.ShowResult)
                         viewModel.dispatch(ResultAction.Init(hiddenBitmap!!))
                     }*/
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

        if (requestCode == REQUEST_CODE) {
            val dispatchable = dispatchble ?: return
            val imageUri = data?.data ?: return

            dispatchable.dispatch(
                StegoAction.HandleContentImagePicked(imageUri.toString())
            )
        } else if (requestCode == CONTAINER_REQUEST_CODE) {
            val dispatchable = dispatchble ?: return
            val imageUri = data?.data ?: return

            dispatchable.dispatch(
                StegoAction.HandleContainerPicked(imageUri.toString())
            )
        }
    }

    override fun getBackStackTag(): String = "ChatFragment"

    companion object {
        const val REQUEST_CODE = 1299
        const val CONTAINER_REQUEST_CODE = 1300
    }
}

fun getPicassoInstance(fragment: Fragment): RequestManager {
    return if (fragment.isAdded && fragment.activity != null) {
        Glide.with(fragment)
    } else {
        Glide.with(MainApplication.getInstance())
    }
}

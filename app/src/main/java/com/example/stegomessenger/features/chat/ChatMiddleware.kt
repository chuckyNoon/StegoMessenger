package com.example.stegomessenger.features.chat

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.domain.StegoAlgorithm
import com.example.stegomessenger.domain.model.HiddenContent
import com.example.stegomessenger.features.chat.items.ImageMessageCell
import com.example.stegomessenger.features.content_dialog.ContentAction
import com.example.stegomessenger.app.core.CoreAction
import com.example.stegomessenger.features.stego_dialog.StegoAction
import com.example.stegomessenger.features.stego_dialog.StegoStateType
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatMiddleware @Inject constructor(
    private val requestManager: RequestManager,
    private val algorithm: StegoAlgorithm
) : Middleware<ChatState>() {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ChatState,
        newState: ChatState
    ) {
        when (action) {
            is ChatAction.ClickSendImage -> handleClickSendImage(newState, dispatchable)
            is ChatAction.ClickSendText ->  handleClickSendText(newState, dispatchable)
            is ChatAction.ClickImage -> handleClickImage(dispatchable, cell = action.cell)
        }
    }

    private fun handleClickImage(dispatchable: Dispatchable, cell: ImageMessageCell){
        val imageUrl = (cell.imageSource as? ImageMessageCell.ImageSource.Url)
            ?.url
            ?: return

        val newTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                middlewareScope.launch {
                    val decoded = algorithm.decodeBitmap(resource)
                    if (decoded == null) {
                        dispatchable.dispatch(CoreAction.ShowToast("Failed to find hidden message"))
                    } else {
                        dispatchable.dispatch(CoreAction.ShowContentDialog)

                        val action = when (decoded) {
                            is HiddenContent.Image ->
                                ContentAction.Init(text = null, image = decoded.bitmap)
                            is HiddenContent.Text ->
                                ContentAction.Init(text = decoded.text, image = null)
                        }
                        dispatchable.dispatch(action)
                    }
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        }

        requestManager
            .asBitmap()
            .load(imageUrl)
            .into(newTarget)
    }

    private fun handleClickSendImage(newState: ChatState, dispatchable: Dispatchable) {
        val chat = newState.chat ?: return

        dispatchable.dispatch(
            StegoAction.Init(
                stateType = StegoStateType.IMAGE,
                receiverId = chat.id
            )
        )
        dispatchable.dispatch(CoreAction.ShowStegoDialog)
    }

    private fun handleClickSendText(newState: ChatState, dispatchable: Dispatchable){
        val chat = newState.chat ?: return

        dispatchable.dispatch(
            StegoAction.Init(
                stateType = StegoStateType.TEXT,
                receiverId = chat.id
            )
        )
        dispatchable.dispatch(CoreAction.ShowStegoDialog)
    }
}

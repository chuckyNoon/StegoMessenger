package com.example.stegomessenger.chat

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.common.launchBackgroundWork
import com.example.stegomessenger.content_dialog.ContentAction
import com.example.stegomessenger.steganography.LsbAlgorithm
import com.example.stegomessenger.main.navigation.CoreAction
import com.example.stegomessenger.stego_dialog.StegoAction
import com.example.stegomessenger.stego_dialog.StegoStateType

class ChatMiddleware(
    private val requestManager: RequestManager
) : Middleware<ChatState> {

    private var loadImageTarget: CustomTarget<Bitmap> ? = null

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
        loadImageTarget ?: return

        val newTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                launchBackgroundWork {
                    val decoded = LsbAlgorithm().lsbDecode(resource)
                    if (decoded == null) {
                        dispatchable.dispatch(CoreAction.ShowToast("Failed to find hidden message"))
                    } else {
                        dispatchable.dispatch(CoreAction.ShowContentDialog)

                        val action = when (decoded) {
                            is LsbAlgorithm.DecodeResult.Image ->
                                ContentAction.Init(text = null, image = decoded.bitmap)
                            is LsbAlgorithm.DecodeResult.Text ->
                                ContentAction.Init(text = decoded.text, image = null)
                        }
                        dispatchable.dispatch(action)
                    }
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                loadImageTarget = null
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

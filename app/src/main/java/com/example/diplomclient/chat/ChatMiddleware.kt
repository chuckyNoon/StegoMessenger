package com.example.diplomclient.chat

import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.redux.dispatcher.Dispatchable
import com.example.diplomclient.arch.redux.store.Middleware
import com.example.diplomclient.stego_dialog.StegoAction
import com.example.diplomclient.stego_dialog.StegoStateType

class ChatMiddleware : Middleware<ChatState> {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: ChatState,
        newState: ChatState
    ) {
        when (action) {
            is ChatAction.ClickSendImage -> {
                val receiverId = newState.chat!!.id
                dispatchable.dispatch(
                    StegoAction.Init(
                        stateType = StegoStateType.IMAGE,
                        receiverId = receiverId
                    )
                )
            }
            is ChatAction.ClickSendText -> {
                val receiverId = newState.chat!!.id
                dispatchable.dispatch(
                    StegoAction.Init(
                        stateType = StegoStateType.TEXT,
                        receiverId = receiverId
                    )
                )
            }
        }
    }
}

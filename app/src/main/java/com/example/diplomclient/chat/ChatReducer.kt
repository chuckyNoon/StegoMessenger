package com.example.diplomclient.chat

import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.MessageCell

class ChatReducer : Reducer<ChatState> {

    override fun acceptsAction(action: Action): Boolean = action is ChatAction

    override fun reduce(oldState: ChatState, action: Action): ChatState =
        when (action) {
            is ChatAction.Init ->
                rebuildViewState(oldState.copy(chat = action.chat))
            is ChatAction.TextTyped ->
                rebuildViewState(oldState.copy(typedText = action.text))
            is ChatAction.ClickSend -> oldState
            else -> oldState
        }

    private fun rebuildViewState(state: ChatState): ChatState {
        val chat = state.chat!!
        val cells = chat.messages.map { message ->
            MessageCell(
                contentText = message.text,
                dateText = "some date"
            )
        }
        val viewState = ChatViewState(
            chatName = chat.name,
            typedText = state.typedText,
            cells = cells,
        )
        return state.copy(viewState = viewState)
    }
}

package com.example.diplomclient.chat

import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.main.navigation.CoreAction
import com.example.diplomclient.overview.model.MessageCell

class ChatReducer : Reducer<ChatState> {

    override fun acceptsAction(action: Action): Boolean =
        action is ChatAction ||
            action is CoreAction.ChatsReloaded

    override fun reduce(oldState: ChatState, action: Action): ChatState =
        when (action) {
            is ChatAction.Init ->
                rebuildViewState(oldState.copy(chat = action.chat))
            is ChatAction.TextTyped ->
                rebuildViewState(oldState.copy(typedText = action.text))
            is ChatAction.CompleteSending ->
                rebuildViewState(
                    oldState.copy(typedText = "", completeEvent = Event(Unit))
                )
            is CoreAction.ChatsReloaded -> {
                val oldChat = requireNotNull(oldState.chat)
                val updatedChat = action.chats.firstOrNull { it.id == oldChat.id }!!
                AppLogger.log("reload chat ins ${oldChat.messages.size}-${updatedChat.messages.size}")
                rebuildViewState(oldState.copy(chat = updatedChat))
            }
            else -> oldState
        }

    private fun rebuildViewState(state: ChatState): ChatState {
        val chat = state.chat!!
        val cells = chat.messages.map { message ->
            MessageCell(
                contentText = message.text,
                dateText = message.createdAtUtcSeconds.toString()
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

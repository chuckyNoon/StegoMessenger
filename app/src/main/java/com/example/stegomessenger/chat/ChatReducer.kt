package com.example.stegomessenger.chat

import com.example.stegomessenger.arch.util.DateTimeFormatter
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.chat.items.TextMessageCell
import com.example.stegomessenger.main.navigation.CoreAction

class ChatReducer(
    private val dateTimeFormatter: DateTimeFormatter
) : Reducer<ChatState> {

    override fun acceptsAction(action: Action): Boolean =
        action is ChatAction ||
            action is CoreAction.ChatsReloaded

    override fun reduce(oldState: ChatState, action: Action): ChatState =
        when (action) {
            is ChatAction.Init ->
                rebuildViewState(oldState.copy(chat = action.chat))
            is CoreAction.ChatsReloaded -> {
                val oldChat = requireNotNull(oldState.chat)
                val updatedChat = action.chats.firstOrNull { it.id == oldChat.id }
                rebuildViewState(oldState.copy(chat = updatedChat))
            }
            else -> oldState
        }

    private fun rebuildViewState(state: ChatState): ChatState {
        val chat = state.chat ?: return state

        val cells = chat.messages.mapNotNull { message ->
            val dateText = dateTimeFormatter.formatDateWithDefaultLocale(
                pattern = "HH-mm",
                millis = message.createdAtUtcSeconds
            )
            when {
                message.text.isNotEmpty() ->
                    TextMessageCell(
                        id = message.createdAtUtcSeconds.toString(),
                        contentText = message.text,
                        dateText = dateText,
                        isMine = message.isMine,
                    )
                message.imageUrl.isNotEmpty() ->
                    ImageMessageCell(
                        id = message.createdAtUtcSeconds.toString() + "img",
                        imageSource = ImageMessageCell.ImageSource.Url(message.imageUrl),
                        dateText = dateText,
                        isMine = message.isMine,
                        isInProgress = false
                    )
                else -> null
            }
        }
        val viewState = ChatViewState(
            chatName = chat.name,
            cells = cells,
        )
        return state.copy(viewState = viewState)
    }
}

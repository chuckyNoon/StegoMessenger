package com.example.diplomclient.chat

import com.example.diplomclient.arch.util.DateTimeFormatter
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.chat.items.ImageMessageCell
import com.example.diplomclient.chat.items.TextMessageCell
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.main.navigation.CoreAction

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
                val updatedChat = action.chats.firstOrNull { it.id == oldChat.id }!!
                AppLogger.log("reload chat ins ${oldChat.messages.size}-${updatedChat.messages.size}")
                rebuildViewState(oldState.copy(chat = updatedChat))
            }
            else -> oldState
        }

    private fun rebuildViewState(state: ChatState): ChatState {
        val chat = state.chat!!

        val cells = chat.messages.mapNotNull { message ->
            if (message.text.isNotEmpty()) {
                TextMessageCell(
                    id = message.createdAtUtcSeconds.toString(),
                    contentText = message.text,
                    dateText = dateTimeFormatter.formatDateWithDefaultLocale(
                        pattern = "HH-mm",
                        millis = message.createdAtUtcSeconds
                    ),
                    isMine = message.isMine,
                )
            } else if (message.imageUrl.isNotEmpty()) {
                ImageMessageCell(
                    id = message.createdAtUtcSeconds.toString() + Math.random().toInt(),
                    imageSource = ImageMessageCell.ImageSource.Url(message.imageUrl),
                    dateText = dateTimeFormatter.formatDateWithDefaultLocale(
                        pattern = "HH-mm",
                        millis = message.createdAtUtcSeconds
                    ),
                    isMine = message.isMine,
                    isInProgress = false
                )
            } else {
                null
            }
        }
        AppLogger.log(cells.size.toString())
        val viewState = ChatViewState(
            chatName = chat.name,
            cells = cells,
        )
        return state.copy(viewState = viewState)
    }
}

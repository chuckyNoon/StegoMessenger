package com.example.diplomclient.chat

import com.aita.arch.di.regular.DateTimeFormatter
import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.common.AppLogger
import com.example.diplomclient.common.BitmapUtils
import com.example.diplomclient.main.navigation.CoreAction
import com.example.diplomclient.overview.model.TextMessageCell

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
        val cells = chat.messages.map { message ->
            TextMessageCell(
                id = message.createdAtUtcSeconds.toString(),
                contentText = message.text,
                dateText = dateTimeFormatter.formatDateWithDefaultLocale(
                    pattern = "HH-mm",
                    millis = message.createdAtUtcSeconds
                ),
                isMine = message.isMine,
                /*image = if (message.image.isNotEmpty()) {
                    //AppLogger.log("im")
                   // AppLogger.log(message.image.length.toString())
                    BitmapUtils.base64ToBitmap(message.image)
                } else {
                    null
                }*/
            )
        }
        val viewState = ChatViewState(
            chatName = chat.name,
            cells = cells,
        )
        return state.copy(viewState = viewState)
    }
}

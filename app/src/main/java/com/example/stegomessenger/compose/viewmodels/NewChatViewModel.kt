package com.example.stegomessenger.compose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.arch.util.DateTimeFormatter
import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.chat.ChatState
import com.example.stegomessenger.chat.ChatViewState
import com.example.stegomessenger.chat.items.ImageMessageCell
import com.example.stegomessenger.chat.items.TextMessageCell
import com.example.stegomessenger.compose.feature.chat.ChatIntent
import com.example.stegomessenger.compose.repository.ChatsRepository
import com.example.stegomessenger.new_arch.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    val prefs: Prefs,
    val stringsProvider: StringsProvider,
    val dateTimeFormatter: DateTimeFormatter,
    val chatsRepository: ChatsRepository
) : ViewModel(), IntentHandler<ChatIntent> {

    private var state = ChatState.INITIAL
    var viewStateLiveData = MutableLiveData(ChatViewState.INITIAL)

    override fun obtainIntent(intent: ChatIntent) {
        when(intent){
            is ChatIntent.ClickSendImage -> {

            }
            is ChatIntent.ClickSendText -> {

            }
            is ChatIntent.ClickImage -> {

            }
        }
    }

    fun init(chatId: String){
        viewModelScope.launch {
            state = state.copy(
                chat = chatsRepository.findChatById(chatId)!!
            )
            rebuildViewState()
        }
    }

    private fun rebuildViewState() {
        val oldState = state
        val chat = oldState.chat ?: return

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
        state = oldState.copy(viewState = viewState)
        viewStateLiveData.value = state.viewState
    }


}
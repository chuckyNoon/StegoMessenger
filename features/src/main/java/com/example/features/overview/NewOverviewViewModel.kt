package com.example.features.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.infra.DateTimeFormatter
import com.example.core.infra.StringsProvider
import com.example.core.design.items.chat.ChatCell
import com.example.data.chat.ChatsRepository
import com.example.core.arch.IntentHandler
import com.example.core.R
import kotlinx.coroutines.launch

class NewOverviewViewModel(
    val chatsRepository: ChatsRepository,
    val dateTimeFormatter: DateTimeFormatter,
    val stringsProvider: StringsProvider
) : ViewModel(), IntentHandler<OverviewIntent> {

    private var state = OverviewState.INITIAL
    var viewStateLiveData = MutableLiveData(OverviewViewState.INITIAL)

    init {
        viewModelScope.launch {
            val chats = chatsRepository.fetchChats()!!
            state = state.copy(chats = chats)
            rebuildViewState()
        }
    }

    override fun obtainIntent(intent: OverviewIntent) {
       when(intent){
           is OverviewIntent.ClickCell -> {

           }
       }
    }

    private fun rebuildViewState(){
        val state = state

        val cells = state.chats.flatMap { chat ->
            val topMessage = chat.messages.maxByOrNull { it.createdAtUtcSeconds }!!
            val chatCell = ChatCell(
                id = chat.id,
                title = chat.name,
                date = dateTimeFormatter.formatDateWithDefaultLocale(
                    pattern = "HH-mm",
                    millis = topMessage.createdAtUtcSeconds
                ),
                message = if (topMessage.imageUrl.isNotEmpty()) {
                    stringsProvider.getString(R.string.image___)
                } else if (topMessage.text.isEmpty()) {
                    stringsProvider.getString(R.string.new_conversation)
                } else {
                    topMessage.text
                },
                initials = buildInitialsFromName(chat.name)
            )
            listOf(chatCell)
        }
        val viewState = OverviewViewState(
            isLoading = state.chats.isEmpty() && state.isLoading,
            cells = cells
        )

        viewStateLiveData.value = viewState
    }


    private fun buildInitialsFromName(name: String): String {
        val initials = name.split(" ")
        val firstInitial = initials[0].uppercase()
        return if (initials.size > 1) {
            firstInitial + initials[1].uppercase()
        } else {
            firstInitial
        }
    }
}
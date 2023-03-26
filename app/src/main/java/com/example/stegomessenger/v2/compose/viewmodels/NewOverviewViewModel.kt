package com.example.stegomessenger.v2.compose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.R
import com.example.stegomessenger.v2.common.infra.DateTimeFormatter
import com.example.stegomessenger.v2.common.infra.StringsProvider
import com.example.stegomessenger.v2.compose.feature.overview.OverviewIntent
import com.example.stegomessenger.v2.compose.model.ChatCell
import com.example.stegomessenger.v2.compose.model.OverviewState
import com.example.stegomessenger.v2.compose.model.OverviewViewState
import com.example.stegomessenger.v2.compose.repository.ChatsRepository
import com.example.stegomessenger.v2.new_arch.IntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewOverviewViewModel @Inject constructor(
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
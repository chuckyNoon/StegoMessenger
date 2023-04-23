package com.example.stegomessenger.overview

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.DateTimeFormatter
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.main.navigation.CoreAction
import com.example.stegomessenger.overview.items.ChatCell
import com.example.stegomessenger.overview.items.DividerCell
import javax.inject.Inject

class OverviewReducer @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter,
    private val stringsProvider: StringsProvider
) : Reducer<OverviewState> {

    override fun acceptsAction(action: Action): Boolean =
        action is OverviewAction ||
                action is CoreAction.ChatsReloaded

    override fun reduce(oldState: OverviewState, action: Action): OverviewState =
        when (action) {
            is OverviewAction.ChatsLoadingStarted ->
                rebuildViewState(oldState.copy(isLoading = true))
            is OverviewAction.ChatsLoadingFail ->
                rebuildViewState(oldState.copy(isLoading = false))
            is CoreAction.ChatsReloaded ->
                rebuildViewState(oldState.copy(isLoading = false, chats = action.chats))
            else -> oldState
        }

    private fun rebuildViewState(state: OverviewState): OverviewState {
        val cells = state.chats.flatMap { chat ->
            val topMessage = chat.messages.maxByOrNull { it.createdAtUtcSeconds }!!
            val chatCell = ChatCell(
                id = chat.id,
                chatTitleText = chat.name,
                dateText = dateTimeFormatter.formatDateWithDefaultLocale(
                    pattern = "HH-mm",
                    millis = topMessage.createdAtUtcSeconds
                ),
                messageText = if (topMessage.imageUrl.isNotEmpty()) {
                    stringsProvider.getString(R.string.image___)
                } else if (topMessage.text.isEmpty()) {
                    stringsProvider.getString(R.string.new_conversation)
                } else {
                    topMessage.text
                },
                initialsText = buildInitialsFromName(chat.name)
            )
            listOf(chatCell, DividerCell)
        }
        val viewState = OverviewViewState(
            isLoading = state.chats.isEmpty() && state.isLoading,
            cells = cells
        )
        return state.copy(viewState = viewState)
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

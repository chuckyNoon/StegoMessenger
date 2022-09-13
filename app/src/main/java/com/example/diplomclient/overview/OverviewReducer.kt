package com.example.diplomclient.overview

import com.example.diplomclient.arch.util.DateTimeFormatter
import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.arch.redux.Action
import com.example.diplomclient.main.navigation.CoreAction
import com.example.diplomclient.overview.model.items.ChatCell
import com.example.diplomclient.overview.model.DividerCell

class OverviewReducer(
    private val dateTimeFormatter: DateTimeFormatter
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
                    "Изображение..."
                } else if (topMessage.text.isEmpty()) {
                    "Новый диалог"
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

    private fun buildInitialsFromName(name:String) : String{
        val initials = name.split(" ")
        val firstInitial = initials[0].uppercase()
        return if (initials.size > 1) {
            firstInitial + initials[1].uppercase()
        } else {
            firstInitial
        }
    }
}

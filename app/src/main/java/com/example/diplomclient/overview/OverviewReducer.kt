package com.example.diplomclient.overview

import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.ChatCell
import com.example.diplomclient.overview.model.DividerCell

class OverviewReducer : Reducer<OverviewState> {

    override fun acceptsAction(action: Action): Boolean = action is OverviewAction

    override fun reduce(oldState: OverviewState, action: Action): OverviewState =
        when (action) {
            is OverviewAction.ChatsLoadingStarted ->
                rebuildViewState(oldState.copy(isLoading = true))
            is OverviewAction.ChatsLoadingSuccess ->
                rebuildViewState(oldState.copy(isLoading = false, chats = action.chats))
            is OverviewAction.ChatsLoadingFail ->
                rebuildViewState(oldState.copy(isLoading = false))
            else -> oldState
        }

    private fun rebuildViewState(state: OverviewState): OverviewState {
        val cells = state.chats.flatMap { chat ->
            val topMessage = chat.messages.maxByOrNull { it.createdAtUtcSeconds }
            val chatCell = ChatCell(
                id = chat.id,
                nameText = chat.name,
                dateText = topMessage?.text ?: "22.02.2022"
            )
            listOf(chatCell, DividerCell)
        }
        val viewState = OverviewViewState(
            isLoading = state.isLoading,
            cells = cells
        )
        return state.copy(viewState = viewState)
    }
}

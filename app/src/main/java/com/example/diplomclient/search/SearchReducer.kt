package com.example.diplomclient.search

import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.overview.model.DividerCell
import com.example.diplomclient.search.item.SearchUserCell

class SearchReducer : Reducer<SearchState> {

    override fun acceptsAction(action: Action): Boolean = action is SearchAction

    override fun reduce(oldState: SearchState, action: Action): SearchState =
        when (action) {
            is SearchAction.TextTyped ->
                rebuildViewState(oldState.copy(typedId = action.text))
            is SearchAction.UsersLoaded ->
                rebuildViewState(oldState.copy(matchingUsers = action.matchingUsers))
            is SearchAction.Back ->
                oldState.copy(backEvent = Event(Unit))
            else -> oldState
        }

    private fun rebuildViewState(state: SearchState): SearchState {
        val cells = state.matchingUsers.flatMap { matchingUser ->
            listOf(
                SearchUserCell(
                    idText = "@" + matchingUser.id,
                    nameText = matchingUser.name
                ),
                DividerCell
            )
        }
        val viewState = SearchViewState(
            searchText = state.typedId,
            cells = cells
        )
        return state.copy(viewState = viewState)
    }
}

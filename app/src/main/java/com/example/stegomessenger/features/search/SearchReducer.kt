package com.example.stegomessenger.features.search

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.util.Event
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.common.ColoredText
import com.example.stegomessenger.features.overview.items.DividerCell
import com.example.stegomessenger.features.search.item.SearchUserCell
import javax.inject.Inject

class SearchReducer @Inject constructor() : Reducer<SearchState> {

    override fun acceptsAction(action: Action): Boolean = action is SearchAction

    override fun reduce(oldState: SearchState, action: Action): SearchState =
        when (action) {
            is SearchAction.TextTyped ->
                rebuildViewState(oldState.copy(typedText = action.text))
            is SearchAction.UsersLoaded ->
                rebuildViewState(oldState.copy(searchResults = action.searchResults))
            is SearchAction.Back ->
                oldState.copy(backEvent = Event(Unit))
            else -> oldState
        }

    private fun rebuildViewState(state: SearchState): SearchState {
        val typedText = state.typedText
        if (typedText.isNullOrEmpty()) {
            return state.copy(viewState = SearchViewState.EMPTY)
        }

        val cells = state.searchResults.flatMap { matchingUser ->
            when {
                matchingUser.id.startsWith(typedText) -> {
                    val idText = "@" + matchingUser.id
                    listOf(
                        SearchUserCell(
                            id = matchingUser.id,
                            nameText = ColoredText(
                                value = matchingUser.name,
                                colorRes = R.color.black
                            ),
                            idText = ColoredText(
                                value = idText,
                                startIndex = 0,
                                endIndex = typedText.length + 1,
                                colorRes = R.color.system_blue
                            ),
                        ),
                        DividerCell
                    )
                }
                matchingUser.name.startsWith(typedText) -> {
                    listOf(
                        SearchUserCell(
                            id = matchingUser.id,
                            nameText = ColoredText(
                                value = matchingUser.name,
                                endIndex = typedText.length,
                                colorRes = R.color.system_blue
                            ),
                            idText = ColoredText(
                                value = matchingUser.id,
                                colorRes = R.color.contentSecondary
                            ),
                        ),
                        DividerCell
                    )
                }
                else -> {
                    emptyList()
                }
            }
        }
        val viewState = SearchViewState(
            searchText = state.typedText,
            cells = cells
        )
        return state.copy(viewState = viewState)
    }
}

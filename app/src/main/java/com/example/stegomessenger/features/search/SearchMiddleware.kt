package com.example.stegomessenger.features.search

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.dispatcher.Dispatchable
import com.example.stegomessenger.arch.redux.store.Middleware
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.StringsProvider
import com.example.stegomessenger.data.chat.ChatsRepository
import com.example.stegomessenger.data.search.SearchRepository
import com.example.stegomessenger.app.core.CoreAction
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMiddleware @Inject constructor(
    private val searchRepository: SearchRepository,
    private val chatRepository: ChatsRepository,
    private val stringsProvider: StringsProvider
) : Middleware<SearchState>() {

    override fun onReduced(
        dispatchable: Dispatchable,
        action: Action,
        oldState: SearchState,
        newState: SearchState
    ) {
        when (action) {
            is SearchAction.ClickStartChat -> startNewChat(dispatchable, userId = action.cell.id)
            is SearchAction.TextTyped -> loadMatchingUsers(dispatchable, action.text)
        }
    }

    private fun startNewChat(
        dispatchable: Dispatchable,
        userId: String
    ) = middlewareScope.launch {
        runCatching { chatRepository.startNewChat(userId) }
            .onSuccess {
                dispatchable.dispatch(CoreAction.ReloadChats)
                dispatchable.dispatch(SearchAction.Back)
            }
            .onFailure {
                dispatchable.dispatch(
                    CoreAction.ShowToast(
                        it.message ?: stringsProvider.getString(R.string.error)
                    )
                )
            }
    }

    private fun loadMatchingUsers(
        dispatchable: Dispatchable,
        typedText: String
    ) = middlewareScope.launch {
        runCatching { searchRepository.searchByQuery(typedText) }
            .onSuccess {
                dispatchable.dispatch(SearchAction.UsersLoaded(searchResults = it))
            }
    }
}

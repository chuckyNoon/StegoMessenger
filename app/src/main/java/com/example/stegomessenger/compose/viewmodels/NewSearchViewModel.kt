package com.example.stegomessenger.compose.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stegomessenger.R
import com.example.stegomessenger.common.ColoredText
import com.example.stegomessenger.compose.feature.search.SearchIntent
import com.example.stegomessenger.compose.repository.MatchingUsersRepository
import com.example.stegomessenger.new_arch.IntentHandler
import com.example.stegomessenger.overview.model.items.DividerCell
import com.example.stegomessenger.search.SearchState
import com.example.stegomessenger.search.SearchViewState
import com.example.stegomessenger.search.item.SearchUserCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewSearchViewModel @Inject constructor(
    val matchingUsersRepository: MatchingUsersRepository
) : ViewModel(), IntentHandler<SearchIntent> {

    private var state = SearchState.INITIAL
    var viewStateLiveData = MutableLiveData(SearchViewState.INITIAL)

    override fun obtainIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.ClickStartChat -> {

            }
            is SearchIntent.TextTyped -> {
                state = state.copy(typedText = intent.text)
                rebuildViewState()
                viewModelScope.launch{
                    val users  = matchingUsersRepository.fetchUsersByText(intent.text)
                    state = state.copy(matchingUsers = users)
                    rebuildViewState()
                }
            }
            is SearchIntent.Back -> {

            }
        }
    }

    private fun rebuildViewState() {
        val oldState = state
        val typedText = oldState.typedText

        if (typedText.isNullOrEmpty()) {
            viewStateLiveData.value = SearchViewState.INITIAL
            return
        }

        val cells = oldState.matchingUsers.flatMap { matchingUser ->
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
            searchText = oldState.typedText,
            cells = cells
        )

        state = oldState.copy(viewState = viewState)
        viewStateLiveData.value = state.viewState
    }

}
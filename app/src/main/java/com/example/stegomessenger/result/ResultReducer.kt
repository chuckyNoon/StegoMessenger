package com.example.stegomessenger.result

import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.Action
import javax.inject.Inject

class ResultReducer @Inject constructor() : Reducer<ResultState> {

    override fun acceptsAction(action: Action): Boolean = action is ResultAction

    override fun reduce(oldState: ResultState, action: Action): ResultState =
        when (action) {
            is ResultAction.Init ->
                rebuildViewState(oldState.copy(bitmap = action.bitmap))
            else -> oldState
        }

    private fun rebuildViewState(state: ResultState): ResultState {
        val viewState = ResultViewState(
            bitmap = state.bitmap
        )
        return state.copy(viewState = viewState)
    }
}

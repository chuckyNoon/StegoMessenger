package com.example.diplomclient.test

import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action
import com.example.diplomclient.test.model.MessageCell

class TestReducer : Reducer<TestState> {

    override fun acceptsAction(action: Action): Boolean = action is TestAction

    override fun reduce(oldState: TestState, action: Action): TestState =
        when (action) {
            is TestAction.DataLoaded ->
                rebuildViewState(
                    oldState.copy(
                        values = oldState.values + action.value
                    )
                )
            else -> oldState
        }

    private fun rebuildViewState(state: TestState): TestState {
        val cells = state.values.map { MessageCell(it) }
        return state.copy(viewState = TestViewState(cells = cells))
    }
}

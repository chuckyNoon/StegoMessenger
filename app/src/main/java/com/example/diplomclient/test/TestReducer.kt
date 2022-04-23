package com.example.diplomclient.test

import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action

class TestReducer : Reducer<TestState> {

    override fun acceptsAction(action: Action): Boolean = action is TestAction

    override fun reduce(oldState: TestState, action: Action): TestState =
        when (action) {
            is TestAction.Init -> oldState.copy(
                viewState = oldState.viewState.copy(
                    text = "123124"
                )
            )
            else -> oldState
        }
}

package com.example.diplomclient.main

import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
import com.example.diplomclient.arch.flux.Action

class MainReducer : Reducer<MainState> {

    override fun acceptsAction(action: Action): Boolean =
        action is MainAction

    override fun reduce(oldState: MainState, action: Action): MainState =
        when (action) {
            is MainAction.ShowTestFragment ->
                oldState.copy(navigationEvent = Event(MainNavigation.Test))
            else -> oldState
        }
}

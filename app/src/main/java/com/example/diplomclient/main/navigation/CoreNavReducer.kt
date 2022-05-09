package com.example.diplomclient.main.navigation

import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
import com.example.diplomclient.arch.flux.Action

class CoreNavReducer : Reducer<CoreNavState> {

    override fun acceptsAction(action: Action): Boolean =
        action is CoreNavAction

    override fun reduce(oldState: CoreNavState, action: Action): CoreNavState =
        when (action) {
            is CoreNavAction.ShowTestFragment ->
                oldState.copy(navigationEvent = Event(CoreNav.Test))
            is CoreNavAction.ShowLogin ->
                oldState.copy(navigationEvent = Event(CoreNav.Login))
            is CoreNavAction.ShowRegistration ->
                oldState.copy(navigationEvent = Event(CoreNav.Registration))
            is CoreNavAction.ShowError ->
                oldState.copy(errorEvent = Event(action.text))
            else -> oldState
        }
}

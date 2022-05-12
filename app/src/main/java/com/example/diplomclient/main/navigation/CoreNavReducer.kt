package com.example.diplomclient.main.navigation

import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
import com.example.diplomclient.arch.flux.Action

class CoreNavReducer : Reducer<CoreNavState> {

    override fun acceptsAction(action: Action): Boolean =
        action is CoreAction

    override fun reduce(oldState: CoreNavState, action: Action): CoreNavState =
        when (action) {
            is CoreAction.ShowOverviewFragment ->
                oldState.copy(navigationEvent = Event(CoreNav.Overview))
            is CoreAction.ShowLogin ->
                oldState.copy(navigationEvent = Event(CoreNav.Login))
            is CoreAction.ShowRegistration ->
                oldState.copy(navigationEvent = Event(CoreNav.Registration))
            is CoreAction.ShowError ->
                oldState.copy(errorEvent = Event(action.text))
            is CoreAction.ShowChat ->
                oldState.copy(navigationEvent = Event(CoreNav.Chat))
            is CoreAction.ShowSearch ->
                oldState.copy(navigationEvent = Event(CoreNav.Search))
            else -> oldState
        }
}

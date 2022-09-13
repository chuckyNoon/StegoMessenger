package com.example.diplomclient.main.navigation

import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.arch.redux.util.Event
import com.example.diplomclient.arch.redux.Action

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
            is CoreAction.ShowToast ->
                oldState.copy(errorEvent = Event(action.text))
            is CoreAction.ShowChat ->
                oldState.copy(navigationEvent = Event(CoreNav.Chat))
            is CoreAction.ShowSearch ->
                oldState.copy(navigationEvent = Event(CoreNav.Search))
            is CoreAction.ShowResult ->
                oldState.copy(navigationEvent = Event(CoreNav.Result))
            is CoreAction.ShowStegoDialog ->
                oldState.copy(navigationEvent = Event(CoreNav.StegoDialog))
            is CoreAction.ShowContentDialog ->
                oldState.copy(navigationEvent = Event(CoreNav.ContentDialog))
            else -> oldState
        }
}

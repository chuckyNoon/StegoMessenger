package com.example.diplomclient.stego_dialog

import com.aita.arch.store.Reducer
import com.example.diplomclient.arch.flux.Action

class StegoReducer : Reducer<StegoState> {

    override fun acceptsAction(action: Action): Boolean = action is StegoAction

    override fun reduce(oldState: StegoState, action: Action): StegoState = when (action) {
        is StegoAction.Init ->
            rebuildViewState(
                oldState.copy(receiverId = action.receiverId)
            )
        is StegoAction.HandleImagePicked ->
            rebuildViewState(
                oldState.copy(imageUriStr = action.imageUriStr)
            )
        else -> oldState
    }

    private fun rebuildViewState(state: StegoState): StegoState {
        val viewState = StegoViewState(
            isSendButtonAvailable = state.imageUriStr != null,
            imageButtonText = if (state.imageUriStr != null) {
                "Image selected"
            } else {
                "Select image"
            }
        )
        return state.copy(viewState = viewState)
    }
}

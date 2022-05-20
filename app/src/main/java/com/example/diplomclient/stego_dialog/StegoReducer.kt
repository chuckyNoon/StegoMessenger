package com.example.diplomclient.stego_dialog

import com.aita.arch.store.Reducer
import com.aita.arch.util.Event
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
        is StegoAction.HandleContainerPicked ->
            rebuildViewState(
                oldState.copy(containerUriStr = action.containerUriStr)
            )
        is StegoAction.UpdateDisplayImage ->
            rebuildViewState(
                oldState.copy(displayBitmap = action.bitmap)
            )
        is StegoAction.ImageSendingStarted ->
            rebuildViewState(
                oldState.copy(isInPgoress = true)
            )
        is StegoAction.ImageSendingFail ->
            rebuildViewState(
                oldState.copy(isInPgoress = false)
            )
        is StegoAction.ImageSendingSuccess ->
            rebuildViewState(
                oldState.copy(
                    closeEvent = Event(Unit),
                    isInPgoress = false,
                )
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
            },
            containerButtonText = if (state.containerUriStr != null) {
                "Container selected"
            } else {
                "Select container"
            },
            displayBitmap = state.displayBitmap,
            isInPgoress = state.isInPgoress
        )
        return state.copy(viewState = viewState)
    }
}

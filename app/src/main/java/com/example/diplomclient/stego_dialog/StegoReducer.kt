package com.example.diplomclient.stego_dialog

import com.example.diplomclient.arch.redux.store.Reducer
import com.example.diplomclient.arch.redux.util.Event
import com.example.diplomclient.arch.redux.Action

class StegoReducer : Reducer<StegoState> {

    override fun acceptsAction(action: Action): Boolean = action is StegoAction

    override fun reduce(oldState: StegoState, action: Action): StegoState = when (action) {
        is StegoAction.Init ->
            rebuildViewState(
                oldState.copy(
                    stateType = action.stateType,
                    receiverId = action.receiverId,
                )
            )
        is StegoAction.HandleContentImagePicked ->
            rebuildViewState(
                oldState.copy(contentUriStr = action.imageUriStr)
            )
        is StegoAction.HandleContentTextChanged ->
            rebuildViewState(
                oldState.copy(contentText = action.text)
            )
        is StegoAction.HandleContainerPicked ->
            rebuildViewState(
                oldState.copy(containerUriStr = action.containerUriStr)
            )
        is StegoAction.SendingStarted ->
            rebuildViewState(
                oldState.copy(isInPgoress = true)
            )
        is StegoAction.SendingFail ->
            rebuildViewState(
                oldState.copy(isInPgoress = false)
            )
        is StegoAction.SendingSuccess ->
            rebuildViewState(
                oldState.copy(
                    closeEvent = Event(Unit),
                    isInPgoress = false,
                )
            )
        is StegoAction.ClickCheckBox ->
            rebuildViewState(
                oldState.copy(isStegoSelected = !oldState.isStegoSelected)
            )
        is StegoAction.Close ->
            oldState.copy(closeEvent = Event(Unit))

        else -> oldState
    }

    private fun rebuildViewState(state: StegoState): StegoState {
        val viewState = when (state.stateType) {
            StegoStateType.TEXT -> buildTextViewState(state)
            StegoStateType.IMAGE -> buildImageViewState(state)
        }
        return state.copy(viewState = viewState)
    }

    private fun buildTextViewState(stegoState: StegoState): StegoViewState =
        StegoViewState.Text(
            titleText = "Текстовое сообщение",
            isStegoCheckBoxSelected = stegoState.isStegoSelected,
            containerBitmapUriStr = stegoState.containerUriStr,
            isSendButtonEnabled = !stegoState.contentText.isNullOrEmpty(),
            isProgressBarVisible = stegoState.isInPgoress,
            contentText = stegoState.contentText
        )

    private fun buildImageViewState(stegoState: StegoState): StegoViewState =
        StegoViewState.Image(
            titleText = "Визуальное сообщение",
            isStegoCheckBoxSelected = stegoState.isStegoSelected,
            containerBitmapUriStr = stegoState.containerUriStr,
            isSendButtonEnabled = !stegoState.contentUriStr.isNullOrEmpty(),
            isProgressBarVisible = stegoState.isInPgoress,
            contentBitmapUriStr = stegoState.contentUriStr
        )
}

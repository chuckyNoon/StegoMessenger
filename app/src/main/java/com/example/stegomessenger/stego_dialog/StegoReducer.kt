package com.example.stegomessenger.stego_dialog

import com.example.stegomessenger.R
import com.example.stegomessenger.arch.redux.store.Reducer
import com.example.stegomessenger.arch.redux.util.Event
import com.example.stegomessenger.arch.redux.Action
import com.example.stegomessenger.arch.util.StringsProvider

class StegoReducer(
    private val stringsProvider: StringsProvider
) : Reducer<StegoState> {

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
            titleText = stringsProvider.getString(R.string.text_message),
            isStegoCheckBoxSelected = stegoState.isStegoSelected,
            containerBitmapUriStr = stegoState.containerUriStr,
            isSendButtonEnabled = !stegoState.contentText.isNullOrEmpty(),
            isProgressBarVisible = stegoState.isInPgoress,
            contentText = stegoState.contentText
        )

    private fun buildImageViewState(stegoState: StegoState): StegoViewState =
        StegoViewState.Image(
            titleText = stringsProvider.getString(R.string.visual_message),
            isStegoCheckBoxSelected = stegoState.isStegoSelected,
            containerBitmapUriStr = stegoState.containerUriStr,
            isSendButtonEnabled = !stegoState.contentUriStr.isNullOrEmpty(),
            isProgressBarVisible = stegoState.isInPgoress,
            contentBitmapUriStr = stegoState.contentUriStr
        )
}
